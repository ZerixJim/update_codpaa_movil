<?php

/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 5/08/14
 * Time: 12:22
 */

ob_start();

session_start();

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');
$manager = new bdManager();
//$imagen = $_GET['imagen'];
$idfoto = $_GET['idfoto'];

function RotateImageRight($image)
{

    // Now that it's auto-rotated, make sure the EXIF data is correct in case the EXIF gets saved with the image!
    $image->setImageOrientation(imagick::ORIENTATION_RIGHTTOP); // Orientation 6

}

function RotateImageLeft($image)
{

    // Now that it's auto-rotated, make sure the EXIF data is correct in case the EXIF gets saved with the image!
    $image->setImageOrientation(imagick::ORIENTATION_LEFTTOP); // Orientation 5 

}

$sql = "SELECT pc.idphotoCatalogo,concat(m.grupo,' ',m.sucursal) as nombreTienda,e.nombre, pro.nombre as nombrePromo,pc.fecha, pc.imagen, tp.descripcion as nombreTipo, mar.nombre as nombreMarca,concat(sup.nombreSupervisor,' ',apellidoSupervisor) as nombreSuper,m.idTienda

                From photoCatalogo as pc

                inner join maestroTiendas as m on pc.id_tienda=m.idTienda

                inner join tipoExhibicion as e on pc.id_exhibicion=e.idExhibicion

                inner join Promotores as pro on pc.id_promotor=pro.idCelular
				
				left join Supervisores as sup on sup.idSupervisores=pro.Supervisor

                left join tipoPromotor as tp on (tp.idtipoPromotor=pro.idtipoPromotor)

                inner join Marca as mar on mar.idMarca=pc.id_marca

                where pc.idphotoCatalogo=" . $idfoto . " ";

$result_foto = $manager->ejecutarConsulta($sql);

$datos_foto = mysqli_fetch_array($result_foto);


?>


<!Doctype html>

<html>

<head>

    <style>

    </style>

    <script language="javascript" type="application/javascript">

    </script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true"></script>

</head>

<body>

<table width="95%" border="0" bordercolor="#B1B1B1" cellspacing="0">
    <? //$exif = exif_read_data('../../'.$imagen);

    try {
        $image = new Imagick('http://plataformavanguardia.net/' . $datos_foto['imagen']);
        $orientation = $image->getImageOrientation();

        if (isset($_REQUEST['rotation']) && $_REQUEST['rotation'] == "left") {
            RotateImageLeft($image);
            $image->writeImage('../../../' . $datos_foto['imagen']);
            unset($_REQUEST['rotation']);
        }

        if (isset($_REQUEST['rotation']) && $_REQUEST['rotation'] == "right") {
            RotateImageRight($image);
            $image->writeImage('../../../' . $datos_foto['imagen']);

            unset($_REQUEST['rotation']);
        }

        $image->setImageOrientation(imagick::ORIENTATION_BOTTOMRIGHT);
    } catch (Exception $e) {
        echo '<script type="text/javascript">
	console.log("PHP mensaje: No es posible rotar la imagen")
	</script>';
    }

    $sql_tienda = "select t.*,e.nombre,mun.nombre as nom_mun from maestroTiendas t
 left join estados e on (t.idEstado=e.id) 
 left join municipios mun on (mun.id=t.id_municipio)
 where idTienda='" . $datos_foto['idTienda'] . "'";
    $resul_tienda = $manager->ejecutarConsulta($sql_tienda);
    $datos_tienda = mysqli_fetch_array($resul_tienda);

    ?>
    <tr>
        <td width="55%">


            <div id="contenedorImg<? echo $idfoto ?>" class="<? if ($exif['Orientation'] == 6) {
                echo "thumbnail";
            } else {
                echo "thumbnail2";
            } ?>">
                <div class="image" onClick="zoom(this);" id="<? if ($exif['Orientation'] == 6) {
                    echo "rotate";
                } else {
                    echo "fine";
                } ?>">
                    <?

                    if ($orientation == 6) {

                        echo '<img id="img_content" class="rotate-rightF" src="http://plataformavanguardia.net/' . $datos_foto['imagen'] . '" exif="true" />';

                    } else if ($orientation == 5) {
                        echo '<img id="img_content" class="rotate-leftF" src="http://plataformavanguardia.net/' . $datos_foto['imagen'] . '" exif="true" />';
                    } else {

                        echo '<img id="img_content" width="80%" src="http://plataformavanguardia.net/' . $datos_foto['imagen'] . '" exif="true"  />';

                    }
                    ?>
                </div>

            </div>
        </td>


        <td width="45%">
            <div class="datos_foto">
                <table width="100%">
                    <tr>
                        <td colspan="8">
                            <div>
                                <img src='../../imagenes/LogoSVSsinfondo(opacity58).png' width='210px' height='80px'/>
                            </div>
                            <span><strong>Opciones para Imagen</strong></span>

                            <a href="#" onClick="reload('<? echo $idfoto; ?>')"><img
                                    src="../../imagenes/reload.png"/></a> &nbsp;&nbsp;
                            <span>Rotar a la Derecha: </span><input type="checkbox" id="rotar-right" name="rotar-right"
                                                                    onChange="check_out(this.id);"/> &nbsp;&nbsp;
                            <span>Rotar a la Izquierda: </span><input type="checkbox" id="rotar-left" name="rotar-left"
                                                                      onChange="check_out(this.id);"/> &nbsp;&nbsp;
                            <span>Acercar: </span><input type="checkbox" id="acercar" name="acercar"
                                                         onChange="check_out(this.id);"/>

                        </td>
                    </tr>
                    <tr>
                        <td colspan='8'>
                            <strong>DATOS DE LA FOTO</strong><br><br>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table class="table table-hover table-mc-light-green">
                                <thead>
                                <tr bgcolor="#64B5F6">
                                    <th>
                                        Id_Foto
                                    </th>
                                    <th>
                                        PROMOTOR
                                    </th>
                                    <th>
                                        SUPERVISOR
                                    </th>
                                    <th>
                                        NOMBRE
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>
                                        <? echo $datos_foto['idphotoCatalogo']; ?>
                                        <input type="hidden" value="<? echo $datos_foto['idphotoCatalogo']; ?>"
                                               id="id_foto" name="id_foto"/>
                                    </td>

                                    <td><? echo utf8_encode($datos_foto['nombrePromo']); ?>
                                    </td>

                                    <td>
                                        <? echo utf8_encode($datos_foto['nombreSuper']); ?>
                                    </td>

                                    <td>
                                        <? echo $datos_foto['nombre']; ?>
                                    </td>

                                </tr>
                                </tbody>
                            </table>

                            <table class="table table-hover table-mc-light-green">
                                <thead>
                                <tr bgcolor="#64B5F6">
                                    <th>
                                        FECHA
                                    </th>
                                    <th>
                                        TIPO
                                    </th>
                                    <th>
                                        MARCA
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>
                                        <? echo $datos_foto['fecha']; ?>
                                    </td>
                                    <td>
                                        <? echo $datos_foto['nombreTipo']; ?>
                                    </td>

                                    <td>
                                        <? echo $datos_foto['nombreMarca']; ?>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <? $sql_prods = "Select count(idPhotoCatalogo) as tot_prod from photo_producto pp
						inner join Producto p on (pp.idProducto=p.idProducto) where pp.idPhotoCatalogo='" . $datos_foto['idphotoCatalogo'] . "'";
                                        $res_prods = $manager->ejecutarConsulta($sql_prods);

                                        $dat_prods = mysqli_fetch_array($res_prods);

                                        if ($dat_prods['tot_prod'] > 0) {
                                            ?>
                                            <a href="javascript:void(0)" class="easyui-linkbutton"
                                               iconCls="icon-products" plain="true"
                                               onClick="muestra_prods()">Productos</a>

                                        <? } ?>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                </table>
                <br><br>
                <table width="100%" border="0" bordercolor="#B1B1B1" cellspacing="0">
                    <tr>
                        <td colspan='2'>
                            <strong>DATOS DE LA TIENDA</strong><br><br>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table class="table table-hover table-mc-light-green">
                                <thead>
                                <tr bgcolor="#64B5F6">
                                    <th>
                                        ID
                                    </th>
                                    <th>
                                        TIENDA
                                    </th>
                                    <th>
                                        DIRECCION
                                    </th>

                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td><? echo $datos_tienda['idTienda']; ?>
                                    </td>

                                    <td><? echo $datos_foto['nombreTienda']; ?>
                                    </td>


                                    <td>
                                        <? echo utf8_encode($datos_tienda['direccion']); ?>
                                    </td>

                                </tr>
                                </tbody>
                            </table>

                            <table class="table table-hover table-mc-light-green">
                                <thead>
                                <tr bgcolor="#64B5F6">
                                    <th>
                                        COLONIA
                                    </th>
                                    <th>
                                        ESTADO
                                    </th>
                                    <th>
                                        MUNICIPIO
                                    </th>
                                    <th>
                                        TELEFONO
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>
                                        <? echo $datos_tienda['colonia']; ?>
                                    </td>
                                    <td>
                                        <? echo $datos_tienda['nombre']; ?>
                                    </td>

                                    <td>
                                        <? echo $datos_tienda['nom_mun']; ?>
                                    </td>

                                    <td>
                                        <? echo $datos_tienda['telefono']; ?>
                                    </td>

                                </tr>
                                </tbody>

                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>

                            <div id='Mapa-tienda2'></div>

                        </td>
                    </tr>


                </table>

            </div>

        </td>
    </tr>

</table>
<script language="javascript" type="application/javascript">
    $(document).ready(function () {


        let latit = <?= $datos_tienda['x']; ?>;
        let longi = <?= $datos_tienda['y']; ?>;

        let myLatlng = new google.maps.LatLng(latit, longi);


        let mapOptions = {
            zoom: 15,
            center: myLatlng
        };
        let map = new google.maps.Map(document.querySelector('#Mapa-tienda2'), mapOptions);

        let marker = new google.maps.Marker({
            position: myLatlng,
            map: map,
            title: 'Localizacion Tienda'
        });


    });
    function muestra_prods() {
        let idFoto = $('#id_foto').val();
        document.getElementById('dlg_prodFot').style.display = "";

        $('#dlg_prodFot').dialog({
            title: 'Productos en Fotografia',
            href: '../php/fotos/productos_fot.php?idFoto=' + idFoto,
            width: 650,
            height: 350,
            buttons: [{
                text: 'Ok',
                iconCls: 'icon-ok',
                handler: function () {
                    $('#dlg_prodFot').dialog('close');
                }
            }]
        });

    }

</script>

</body>

</html>

