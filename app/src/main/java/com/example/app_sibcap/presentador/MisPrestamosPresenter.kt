package com.example.app_sibcap.presentador

import com.example.app_sibcap.modelo.Prestamo
import com.example.app_sibcap.modelo.PrestamoRepository
import com.example.app_sibcap.modelo.PrestamoRepositoryImpl
import com.example.app_sibcap.modelo.ResultadoApi
import com.example.app_sibcap.modelo.SesionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MisPrestamosPresenter(
    private val vista: MisPrestamosContract.View,
    private val sesion: SesionManager,
    private val repositorio: PrestamoRepository = PrestamoRepositoryImpl(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) : MisPrestamosContract.Presenter {

    private var todos: List<Prestamo> = emptyList()
    private var filtroActual = FiltroPrestamos.TODOS
    private var cargando = false
    private var datosCargados = false

    override fun cargarPrestamos() {
        if (cargando) return                      // evita solicitudes duplicadas por clics dobles
        val token = sesion.token()
        if (token.isNullOrBlank()) {
            vista.sesionExpirada()
            return
        }

        cargando = true
        vista.mostrarCarga()
        scope.launch {
            val resultado = repositorio.obtenerMisPrestamos(token)
            cargando = false
            vista.ocultarCarga()                  // se oculta tanto en éxito como en error

            when (resultado) {
                is ResultadoApi.Exito -> {
                    todos = resultado.datos.prestamos
                    datosCargados = true
                    // El conteo de vencidos usa la bandera "vencido" que calcula el servidor
                    vista.mostrarResumen(resultado.datos.resumen, todos.count { it.vencido })
                    mostrarSegunFiltro()
                }
                is ResultadoApi.Error -> {
                    if (resultado.codigo == 401) {   // token vencido o inválido
                        sesion.cerrar()
                        vista.sesionExpirada()
                    } else {
                        vista.mostrarError(resultado.mensaje, resultado.sinConexion)
                    }
                }
            }
        }
    }

    override fun filtrar(filtro: FiltroPrestamos) {
        filtroActual = filtro
        mostrarSegunFiltro()
    }

    override fun reintentar() = cargarPrestamos()

    override fun destruir() {
        scope.cancel()
    }

    // Vigentes = activos y a tiempo | Vencidos = pasados de fecha o con multa pendiente
    private fun mostrarSegunFiltro() {
        if (!datosCargados) return                // el selector dispara un evento al crearse
        val lista = when (filtroActual) {
            FiltroPrestamos.TODOS -> todos
            FiltroPrestamos.VIGENTES -> todos.filter { it.idEstatus == 1 && !it.vencido }
            FiltroPrestamos.VENCIDOS -> todos.filter { it.vencido || it.idEstatus == 3 }
        }
        when {
            todos.isEmpty() -> vista.mostrarVacio("Cuando tengas libros prestados, aparecerán aquí.", true)
            lista.isEmpty() -> vista.mostrarVacio(
                if (filtroActual == FiltroPrestamos.VIGENTES) "No tienes préstamos vigentes."
                else "No tienes préstamos vencidos.",
                false
            )
            else -> vista.mostrarPrestamos(lista)
        }
    }
}
