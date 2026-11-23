<?php
/**
 * SIBCAP - Módulo Lector
 * GET detalle_libro.php?id_libro=5  — Responsable: Jhonatan
 *
 * Devuelve todos los datos de un libro desde vista_libros: ISBN, título,
 * sinopsis, portada, autor, categoría, editorial y ejemplares disponibles.
 *
 * Respuesta: { "success", "message", "data": {libro} }
 */
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');

function responder(int $codigoHttp, bool $success, string $message, $data = null): void
{
    http_response_code($codigoHttp);
    echo json_encode(
        ['success' => $success, 'message' => $message, 'data' => $data],
        JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES
    );
    exit;
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

$idCrudo = $_GET['id_libro'] ?? null;
if ($idCrudo === null || $idCrudo === '') {
    responder(400, false, 'Falta el parámetro id_libro.');
}

$idLibro = filter_var($idCrudo, FILTER_VALIDATE_INT, ['options' => ['min_range' => 1]]);
if ($idLibro === false) {
    responder(400, false, 'El id_libro debe ser un número entero mayor a 0.');
}

require_once __DIR__ . '/../config/conexion.php';

try {
    $stmt = $pdo->prepare('SELECT
            `Id_Libro`               AS id_libro,
            `ISBN`                   AS isbn,
            `Titulo`                 AS titulo,
            `Sinopsis`               AS sinopsis,
            `Portada_URL`            AS portada_url,
            `Fecha_Adquisicion`      AS fecha_adquisicion,
            `Id_Autor`               AS id_autor,
            `Autor`                  AS autor,
            `Id_Categoria`           AS id_categoria,
            `Nombre_Categoria`       AS categoria,
            `Id_Editorial`           AS id_editorial,
            `Nombre_Editorial`       AS editorial,
            `Ejemplares_Total`       AS ejemplares_total,
            `Ejemplares_Disponibles` AS ejemplares_disponibles
        FROM `vista_libros`
        WHERE `Id_Libro` = :id
        LIMIT 1');
    $stmt->bindValue(':id', $idLibro, PDO::PARAM_INT);
    $stmt->execute();
    $libro = $stmt->fetch();
} catch (PDOException $e) {
    error_log('detalle_libro.php: ' . $e->getMessage());
    responder(500, false, 'No se pudo consultar el libro. Intenta más tarde.');
}

if (!$libro) {
    responder(404, false, 'El libro no existe.');
}

foreach (['id_libro', 'id_autor', 'id_categoria', 'id_editorial', 'ejemplares_total', 'ejemplares_disponibles'] as $campo) {
    $libro[$campo] = (int) $libro[$campo];
}
$libro['portada_url'] = urlPortada($libro['portada_url']);
$libro['disponible']  = $libro['ejemplares_disponibles'] > 0;

responder(200, true, 'Detalle del libro obtenido correctamente.', $libro);
