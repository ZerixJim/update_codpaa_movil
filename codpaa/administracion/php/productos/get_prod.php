<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');
    include_once('../../php/seguridad.php');

    $tipo = $_SESSION['tipo'];
    $idUsuario = $_SESSION['idUser'];


    $filtro = "";


    if (isset($_REQUEST['idMarca']) and !empty($_REQUEST['idMarca']) ){

        $filtro = "and p.idMarca = " .$_REQUEST['idMarca'] ;


        if ( isset($_REQUEST['producto'])  and !empty($_REQUEST['producto'] )){
            $filtro.= " and p.nombre like '%" . $_REQUEST['producto'] . "%'";
        }

    }else{

        if ( isset($_REQUEST['producto'])  and !empty($_REQUEST['producto'] )){
            $filtro.= " and p.nombre like '%" . $_REQUEST['producto'] . "%'";
        }


    }




    $manager = DataBase::getInstance();
    if ($_SESSION['id_perfil'] == '1') {

        $sql = "select p.idProducto,M.nombre as Marca,p.nombre,p.tipo,p.presentacion, if(p.tester = 1, 'Probador', '') as ck,
        p.estatus, (SELECT COUNT(pf.id_formato) FROM producto_formato AS pf WHERE  pf.id_producto = p.`idProducto`) AS asign,  p.codigoBarras 
        from Producto as p
		inner join Marca M on M.idMarca=p.idMarca

		where p.estatus = 1 AND M.estatus = 1 AND p.idMarca>0  " . $filtro . " ";


    } else if ($_SESSION['id_perfil'] == '5' || $_SESSION['id_perfil'] == '4') {

        $sql = "select p.idProducto,M.nombre as Marca,p.nombre,p.tipo,p.presentacion, if(p.tester = 1, 'Probador', '') as ck,
        p.estatus, (SELECT COUNT(pf.id_formato) FROM producto_formato AS pf WHERE  pf.id_producto = p.`idProducto`) AS asign, p.codigoBarras
        from Producto as p
		inner join Marca M on M.idMarca=p.idMarca

		where p.estatus = 1 AND M.estatus = 1 AND p.idMarca IN (select idMarca from usuariosMarcaAsignada where idUsuario='" .
            $idUsuario . "') " . $filtro . "";

    } else if ($_SESSION['id_perfil'] == '6') {
        $sql = "select p.idProducto,M.nombre as Marca,p.nombre,p.tipo,p.presentacion, if(p.tester = 1, 'Probador', '') as ck,
        p.estatus, (SELECT COUNT(pf.id_formato) FROM producto_formato AS pf WHERE  pf.id_producto = p.`idProducto`) AS asign, p.codigoBarras
        from Producto as p
		inner join Marca M on M.idMarca=p.idMarca

		where p.estatus=1 AND M.estatus = 1 and p.idMarca IN (select idMarca from ClientesMarcas where idCliente=(select idCliente from usuarios where idUsuario='" . $idUsuario . "'))
		" . $filtro . "";

    } else if($_SESSION['id_perfil'] == '3' || $_SESSION['id_perfil'] == '2' || $_SESSION['id_perfil'] == '17'){

        $sql = "select p.idProducto,M.nombre as Marca,p.nombre,p.tipo,p.presentacion, if(p.tester = 1, 'Probador', '') as ck,
        p.estatus, (SELECT COUNT(pf.id_formato) FROM producto_formato AS pf WHERE  pf.id_producto = p.`idProducto`) AS asign, p.codigoBarras
        from Producto as p
		inner join Marca M on M.idMarca=p.idMarca

		where p.estatus=1 AND M.estatus = 1 and p.idMarca IN (select idMarca from usuariosMarcaAsignada where idUsuario='" .
            $idUsuario . "') " . $filtro . "";



    }


    $rs = $manager->ejecutarConsulta($sql);
    $result = array();
    while ($row = mysqli_fetch_object($rs)) {

        $row->nombre = htmlentities($row->nombre);
        array_push($result, $row);
    }

    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT);
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}


