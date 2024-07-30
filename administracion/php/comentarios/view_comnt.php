<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    ?>
    <!doctype html>
    <html>
    <head>
        <meta charset="utf-8">
    </head>

    <body>
    <?
    $idComnt = $_REQUEST['idComnt'];

    include_once('../../connexion/DataBase.php');


    $manager = DataBase::getInstance();

    $query_comnt = "SELECT ct.*,p.nombre AS Promotor,mt.sucursal AS Tienda FROM comentarioTienda ct 
		LEFT JOIN Promotores p ON (ct.idCelular=p.idCelular)
		LEFT JOIN maestroTiendas mt ON (mt.idTienda=ct.idTienda) 
		WHERE idcomentarioTienda='" . $idComnt . "'";

    $resul_comnt = $manager->ejecutarConsulta($query_comnt);

    $datos_comnt = mysqli_fetch_array($resul_comnt);

    ?>
    <div>
        <form id="fm_viewComnt" method="post" class="form-style" novalidate>


            <ul>
                <li>
                    <label>Tienda</label>
                    <strong><? echo "(" . $datos_comnt['idTienda'] . ") " . $datos_comnt['Tienda']; ?></strong>
                </li>
                <li>
                    <label for="prom_comnt">Promotor</label>
                    <input name="prom_comnt" id="prom_comnt" class="easyui-textbox" style=" width:250px;" required
                           data-options="value:'<? echo "(" . $datos_comnt['idCelular'] . ") " . $datos_comnt['Promotor']; ?>'"
                           readonly>
                </li>
                <li>
                    <label for="fecha_comnt">Fecha</label>
                    <input name="fecha_comnt" id="fecha_comnt" class="easyui-textbox"
                           data-options="value:'<? echo $datos_comnt['fecha']; ?>'" readonly>
                </li>
                <li>
                    <label for="comnt">Comentario</label>
                    <input name="comnt" id="comnt" class="easyui-textbox" style=" width:300px; height:60px;"
                           data-options="value:'<? echo $datos_comnt['comentario']; ?>',multiline:true" readonly>
                </li>
                <li>

                </li>
            </ul>


        </form>
    </div>
    </body>
    </html>

    <?
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>