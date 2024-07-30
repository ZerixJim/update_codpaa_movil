<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    $id_prod = $_REQUEST['idProducto'];

    include_once('../../connexion/bdManager.php');
    include_once('../../php/seguridad.php');

    $manager = new bdManager();

    $query_prod = "select P.*,M.nombre as Marca from Producto P
		inner join Marca M on M.idMarca=P.idMarca where idProducto='" . $id_prod . "'";

    $resul_prod = $manager->ejecutarConsulta($query_prod);

    $datos_prod = mysqli_fetch_array($resul_prod);

    ?>
    <div align="center">
        <form id="fm_view" method="post" novalidate>

            <table width="100%">
                <?
                $foto_prod = "../../images/productos/" . $datos_prod['idMarca'] . "/" . $datos_prod["idProducto"] . ".jpg";
                $foto_prod2 = "../../images/productos/" . $datos_prod['idMarca'] . "/" . $datos_prod["idProducto"] . ".png";

                ?>
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <td align="center">
                                    <img src="<? if (file_exists($foto_prod)) {
                                        echo $foto_prod;
                                    } else if (file_exists($foto_prod2)) {
                                        echo $foto_prod2;
                                    } else {
                                        echo "../../imagenes/no_image.jpg";
                                    } ?>" style="width:180px;" exif="true">
                                </td>
                            </tr>
                        </table>

                    </td>


                    <td>
                        <table width="100%">
                            <tr>

                                <div class="fitem">

                                    <td height="40px"><label><h3 class="colorVang">NOMBRE</h3></label>
                                        <? echo $datos_prod['nombre']; ?></td>

                                </div>
                            </tr>
                            <tr>
                                <div class="fitem">

                                    <td height="40px"><label><h3 class="colorVang">MARCA</h3></label>
                                        <? echo $datos_prod['Marca']; ?></td>

                                </div>

                            </tr>
                            <tr>

                                <div class="fitem">

                                    <td height="40px"><label><h3 class="colorVang"> PRESENTACION</h3></label>
                                        <? echo $datos_prod['presentacion']; ?></td>

                                </div>
                            </tr>
                            <tr>
                                <div class="fitem">

                                    <td height="40px"><label><h3 class="colorVang">CODIGO BARRAS</h3></label>
                                        <? echo $datos_prod['codigoBarras']; ?></td>

                                </div>

                            </tr>
                            <tr>

                                <div class="fitem">

                                    <td height="40px"><label><h3 class="colorVang">TIPO PRODUCTO</h3></label>
                                        <? echo $datos_prod['tipo']; ?></td>

                                </div>
                            </tr>
                            <tr>
                                <div class="fitem">

                                    <td height="40px"><label><h3 class="colorVang">MODELO</h3></label>
                                        <? echo $datos_prod['modelo']; ?></td>

                                </div>

                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

        </form>
    </div>

    <?
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>