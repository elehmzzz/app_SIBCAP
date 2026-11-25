package com.example.app_sibcap.presentador

import com.example.app_sibcap.modelo.PrestamoRepository
import com.example.app_sibcap.modelo.PrestamoRepositoryImpl
import com.example.app_sibcap.modelo.ResultadoApi
import com.example.app_sibcap.modelo.SesionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DetallePrestamoPresenter(
    private val vista: DetallePrestamoContract.View,
    private val sesion: SesionManager,
    private val repositorio: PrestamoRepository = PrestamoRepositoryImpl(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) : DetallePrestamoContract.Presenter {

    private var ultimoId = -1
    private var cargando = false

    override fun cargarDetalle(idPrestamo: Int) {
        if (cargando) return
        val token = sesion.token()
        if (token.isNullOrBlank()) {
            vista.sesionExpirada()
            return
        }

        ultimoId = idPrestamo
        cargando = true
        vista.mostrarCarga()
        scope.launch {
            val resultado = repositorio.obtenerDetalle(token, idPrestamo)
            cargando = false
            vista.ocultarCarga()

            when (resultado) {
                is ResultadoApi.Exito -> vista.mostrarDetalle(resultado.datos)
                is ResultadoApi.Error -> {
                    if (resultado.codigo == 401) {
                        sesion.cerrar()
                        vista.sesionExpirada()
                    } else {
                        vista.mostrarError(resultado.mensaje, resultado.sinConexion)   // 403 sin permiso, 404 no existe, etc.
                    }
                }
            }
        }
    }

    override fun reintentar() {
        if (ultimoId > 0) cargarDetalle(ultimoId)
    }

    override fun destruir() {
        scope.cancel()
    }
}
