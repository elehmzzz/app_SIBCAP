<?php
/**
 * SIBCAP - Módulo Lector
 * GET catalogo.php  — Responsable: Jhonatan
 *
 * Parámetros (todos opcionales):
 *   q             texto a buscar en título, autor o ISBN
 *   id_categoria  filtra por categoría
 *   pagina        número de página (desde 1). Default 1
 *   limite        libros por página (1 a 50). Default 20
 *
 * Respuesta (formato del manual): { "status": "success"|"error", "success", "message", "paginacion", "data": [libros] }
 */
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
// Sin caché: el catálogo y la disponibilidad deben ser siempre los actuales
header('Cache-Control: no-store, no-cache, must-revalidate, max-age=0');
header('Pragma: no-cache');
header('Expires: 0');

function responder(int $codigoHttp, bool $success, string $message, $data = null, array $extra = []): void
{
    http_response_code($codigoHttp);
    echo json_encode(
        array_merge(['status' => $success ? 'success' : 'error', 'success' => $success, 'message' => $message], $extra, ['data' => $data]),
        JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES
    );
    exit;
}

/** null si no viene, false si es inválido, int si es válido */
function paramEntero(string $nombre, int $max = PHP_INT_MAX)
{
    if (!isset($_GET[$nombre]) || $_GET[$nombre] === '') {
        return null;
    }
    return filter_var($_GET[$nombre], FILTER_VALIDATE_INT, ['options' => ['min_range' => 1, 'max_range' => $max]]);
}

/** Portada_URL puede ser URL completa o archivo dentro de upload/ */
function urlPortada(?string $valor): ?string
{
    if ($valor === null || trim($valor) === '') {
        return null;
    }
    $valor = trim($valor);
    if (preg_match('#^https?://#i', $valor)) {
        return $valor;
    }
    $https = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off')
        || (($_SERVER['HTTP_X_FORWARDED_PROTO'] ?? '') === 'https');
    $raiz = rtrim(str_replace('\\', '/', dirname(dirname($_SERVER['SCRIPT_NAME']))), '/');
    $valor = ltrim($valor, '/');
    if (strpos($valor, 'upload/') !== 0) {
        $valor = 'upload/' . $valor;
    }
    return ($https ? 'https' : 'http') . '://' . $_SERVER['HTTP_HOST'] . $raiz . '/'
        . implode('/', array_map('rawurlencode', explode('/', $valor)));
}

if (($_SERVER['REQUEST_METHOD'] ?? 'GET') !== 'GET') {
    header('Allow: GET');
    responder(405, false, 'Método no permitido. Usa GET.');
}

$q           = trim((string) ($_GET['q'] ?? ''));
$idCategoria = paramEntero('id_categoria');
$pagina      = paramEntero('pagina') ?? 1;
$limite      = paramEntero('limite', 50) ?? 20;

if ($idCategoria === false) {
    responder(400, false, 'El id_categoria debe ser un número entero mayor a 0.');
}
if ($pagina === false || $limite === false) {
    responder(400, false, 'Parámetros inválidos: pagina debe ser >= 1 y limite entre 1 y 50.');
}
if (mb_strlen($q) > 100) {
    responder(400, false, 'La búsqueda no puede tener más de 100 caracteres.');
}

require_once __DIR__ . '/../config/conexion.php';

// Filtros (con prepares reales no se puede repetir un mismo :parametro)
$condiciones = [];
$params = [];
if ($q !== '') {
    $condiciones[] = '(`Titulo` LIKE :q1 OR `Autor` LIKE :q2 OR `ISBN` LIKE :q3)';
    $params[':q1'] = $params[':q2'] = $params[':q3'] = '%' . $q . '%';
}
if ($idCategoria !== null) {
    $condiciones[] = '`Id_Categoria` = :cat';
    $params[':cat'] = $idCategoria;
}
$where  = $condiciones ? 'WHERE ' . implode(' AND ', $condiciones) : '';
$offset = ($pagina - 1) * $limite;

try {
    // Total para la paginación
    $stmt = $pdo->prepare("SELECT COUNT(*) FROM `vista_libros` $where");
    $stmt->execute($params);
    $total = (int) $stmt->fetchColumn();

    // Libros de la página
    $stmt = $pdo->prepare("SELECT
            `Id_Libro`               AS id_libro,
            `Titulo`                 AS titulo,
            `Autor`                  AS autor,
            `Id_Categoria`           AS id_categoria,
            `Nombre_Categoria`       AS categoria,
            `Portada_URL`            AS portada_url,
            `Ejemplares_Disponibles` AS ejemplares_disponibles
        FROM `vista_libros` $where
        ORDER BY `Titulo` ASC
        LIMIT :limite OFFSET :offset");
    foreach ($params as $nombre => $valor) {
        $stmt->bindValue($nombre, $valor, is_int($valor) ? PDO::PARAM_INT : PDO::PARAM_STR);
    }
    $stmt->bindValue(':limite', $limite, PDO::PARAM_INT);
    $stmt->bindValue(':offset', $offset, PDO::PARAM_INT);
    $stmt->execute();

    $libros = [];
    foreach ($stmt->fetchAll() as $fila) {
        $fila['id_libro']               = (int) $fila['id_libro'];
        $fila['id_categoria']           = (int) $fila['id_categoria'];
        $fila['ejemplares_disponibles'] = (int) $fila['ejemplares_disponibles'];
        $fila['portada_url']            = urlPortada($fila['portada_url']);
        $fila['disponible']             = $fila['ejemplares_disponibles'] > 0;
        $libros[] = $fila;
    }
} catch (PDOException $e) {
    error_log('catalogo.php: ' . $e->getMessage());
    responder(500, false, 'No se pudo consultar el catálogo. Intenta más tarde.');
}

if (count($libros) > 0) {
    $mensaje = 'Catálogo obtenido correctamente.';
} elseif ($q !== '' || $idCategoria !== null) {
    $mensaje = 'No se encontraron libros con esa búsqueda.';
} else {
    $mensaje = 'No hay libros registrados.';
}

// Lista vacía también es 200: la app muestra el estado "vacío", no un error
responder(200, true, $mensaje, $libros, [
    'paginacion' => [
        'pagina'        => $pagina,
        'limite'        => $limite,
        'total'         => $total,
        'total_paginas' => $total > 0 ? (int) ceil($total / $limite) : 0,
    ],
]);
