<?php

/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 23/07/14
 * Time: 11:52
 */


$or = ob_start();

session_start();


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../connexion/DataBase.php');
    include_once('../php/seguridad.php');


    ?>


    <!DOCTYPE html>

    <html lang="es">

    <head>

        <title>Codpaa Web</title>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">


        <link rel="stylesheet" type="text/css" href="../css/s-app-3.css?v=1.1.13">
        <link rel="stylesheet" type="text/css" href="../css/comp-style.css?v=0.0.1"/>
        <link rel="stylesheet" type="text/css" href="../complemento/themes/material-teal/easyui.css">
        <link rel="stylesheet" type="text/css" href="../complemento/themes/icon.css">


        <!--List of scripts-->

        <!--<script src="../complemento/jquery-2.2.0.min.js" type="text/javascript"></script>-->

        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAaDkwcHhqPRmlZWFfPKm4TaKEVZxUbZPw"
                type="text/javascript"></script>

        <script type="text/javascript" src="..//app/bundle-main.js?v=1.1.72"></script>


        <script type="text/javascript" src="..//script/modernizr.custom.js"></script>
        <script type="text/javascript" src="..//script/jquery.dlmenu.js"></script>


        <!--<script type="text/javascript" src="../plugins/easyui/jquery.easyui.min.js"></script>-->

        <script type="text/javascript" src="..//complemento/datagrid-filter.js"></script>
        <script type="text/javascript" src="..//locale/easyui-lang-es.js"></script>
        <script type="text/javascript" src="..//complemento/easyloader.js"></script>


        <script type="text/javascript" src="..//complemento/loader.js"></script>


        <script src="..//plugins/jquery.easing.min.js"></script>


        <script type="text/javascript" src="..//script/fileDownload.js"></script>

        <script type="text/javascript" src="..//plugins/jquery.autocomplete-test.js"></script>
        <script type="text/javascript" src="..//src/jquery.countTo.js"></script>
        <script type="text/javascript" src="..//plugins/multiselect.min.js"></script>


    </head>

    <body>

    <div class="header">
        <!--<header> -->

        <div class="icon-menu-conteiner">

            <img class="side-icon" src="../imagenes/menu.png" alt="menu" style="float: left;"/>

        </div>


        <?

        $imagen = date('m') == '12' ? "../imagenes/favicon-navidad.png" : "../imagenes/favicon.png";


        ?>


        <img src="<?= $imagen; ?>" id="icon-vanguardia" alt="web icon" style="float: left;"/>


        <!--<img src="../imagenes/vanguardia2.png" width="400px" id="logo-vanguardia" style="padding: 5px;"/>-->


        <div class="dropdown">

            <!--<div style="border-radius: 50%; background-color: #ccc;width: 34px;height: 34px;">

            </div>-->

            <button class="dropbtn"></button>

            <div class="dropdown-content">

                <?php
                $nombre = array();
                $nombre = explode(' ', $_SESSION['usuario']);


                echo "<ul>
                         <li><span> " . $nombre[0] . "</span></li>

                         <li><a href='../php/logout.php'>Salir</a></li></ul>";
                ?>

            </div>


        </div>

        <!--   </header>-->
    </div>


    <div id="main-content">


        <div class="easyui-layout" id="layout-main" style="width: 100%; height: 100%;">

            <!--<div  data-options="href:'content_panel_menu.php',region:'west',title:'MENU',split:true"  style="width:15%; height:auto; background-color:#0070A9;"></div>-->


            <!-- <div data-options="region:'north', border:false" id="header-principal" style="position: relative;">



             </div>-->

            <div data-options="region:'center',split:true, border:false" style="width: 100%;">


                <div id="contenedor" class="easyui-tabs" data-options="fit:true">

                    <div title="Dashboard" data-options="closable:false,split:true,href:'dashboard.php'" align="center"
                         style="width:100%;position: relative;background-color: #eeeeee;">
                        <!--<div id="contenido_bienvenido"
                         style="position: absolute; top: 0;bottom: 0;left: 0;right: 0; width: 70%; height: 30%; margin: auto;">


                        <h1> Buen día
                            <span style="text-transform: uppercase;"><? /* echo $nombre[0]; */ ?></span>
                        </h1>

                        <h2 style="color: #999999">Te damos la bienvenida a la interfaz de Codpaa</h2>


                    </div>-->
                    </div>


                </div>
                <div id="dlg_mailUs" style="display:none;">

                    <form id="fm_mailUs" method="post" novalidate>
                        <table>
                            <tr>
                                <td colspan="2">
                                    <span>No tienes ningun correo electronico registrado, registralo por favor.</span>
                                </td>
                            </tr>
                            <tr>

                                <div class="fitem">

                                    <td><label>Email:</label></td>

                                    <td><input title="Email" name="email_us" id="email_us" class="easyui-validatebox"
                                               style="width:180px"
                                               data-options="required:true,validType:'email'"></td>

                                </div>

                            </tr>
                        </table>
                    </form>

                </div>


                <?
                $manager = DataBase::getInstance();

                $sql_mail = "SELECT email FROM usuarios WHERE idUsuario='" . $_SESSION['idUser'] . "'";
                $rs_mail = $manager->ejecutarConsulta($sql_mail);
                $dato_mail = mysqli_fetch_array($rs_mail);

                if ($dato_mail['email'] == "") {
                    ?>
                    <script>
                        document.getElementById('dlg_mailUs').style.display = '';
                        $('#dlg_mailUs').dialog({
                            title: 'Correo Electronico',
                            width: 450,
                            height: 150,
                            buttons: [{
                                text: 'Ok',
                                iconCls: 'icon-ok',
                                handler: function () {
                                    saveMailU();
                                }
                            }, {
                                text: 'Cancel',
                                handler: function () {
                                    $('#dlg_mailUs').dialog('close');
                                }
                            }]
                        });
                    </script>

                <? } ?>


            </div>


        </div>

        <!--Sidebar -->
        <div class="sidebar scroll3" id="sidebar">




            <?php


            /*menu left cuando no es un cliente*/


            if ($_SESSION['id_perfil'] != 6) {
                $c_menus = "SELECT DISTINCT id_menu_cat, categoria, imagen FROM (SELECT DISTINCT menus.id_menu_cat, categoria ,
              menu,menus_cat.imagen, url, menus.estatus FROM menus
		      INNER JOIN permisos_perfil ON menus.id_menu = permisos_perfil.id_menu
		      INNER JOIN menus_cat ON menus.id_menu_cat=menus_cat.id_menu_cat
		      WHERE permisos_perfil.id_perfil = " . $_SESSION['id_perfil'] . ") AS consulta WHERE estatus = 1 ORDER BY categoria ASC ";
                $r_menus = $manager->ejecutarConsulta($c_menus);
                echo '<div class="container-menu">

		            <div id="dl-menu" class="dl-menuwrapper">

			            <ul class="dl-menu">';

                // recorriendo las cotegorias
                while ($a_menus = mysqli_fetch_array($r_menus)) {

                    if (mysqli_num_rows($r_menus) == 0) {


                    } else {
                        echo "<li>";
                        echo "<a><img src='../imagenes/icons/" . $a_menus['imagen'] . ".png' width='24'> " . $a_menus['categoria'] . "</a>";
                        echo "<ul class='dl-submenu'>";

                    }

                    $c_menus2 = "SELECT DISTINCT menus.id_menu_cat, menu ,url, menus.id_menu, menus_cat.*, date(menus.fecha_creacion) fecha 
                                FROM menus 
                                INNER JOIN permisos_perfil
                                ON menus.id_menu = permisos_perfil.id_menu INNER JOIN menus_cat
                                ON menus_cat.id_menu_cat = menus.id_menu_cat WHERE menus.id_menu_cat = '" . $a_menus['id_menu_cat'] . "' AND
                                menus.estatus = 1 AND permisos_perfil.id_perfil = '" . $_SESSION['id_perfil'] . "' AND menus_cat.estatus = 1 ORDER BY menus_cat.id_menu_cat ASC";

                    $r_menus2 = $manager->ejecutarConsulta($c_menus2);

                    $dateNow = date('Y-m-d', strtotime('-30 days'));

                    while ($a_menus2 = mysqli_fetch_array($r_menus2)) {

                        $format = "";

                        if ($dateNow < $a_menus2["fecha"]) {

                            $format = "<span style='color: white; padding: 2px; background-color: #f07831;position: absolute;font-size: 7px;'>NUEVO</span>";

                        }

                        ?>
                        <li style="position: relative">

                            <? echo $format; ?>

                            <a href="#" onClick="addTapMen('<? echo $a_menus2['url']; ?>',
                                    '<?= $a_menus['categoria'] . " / " . $a_menus2['menu']; ?>','<?= $a_menus['imagen']; ?>');">
                                <?= $a_menus2['menu'];

                                ?></a>


                        </li>
                        <?
                        $i = $i + 1;
                    }
                    echo "</ul>";
                    echo "</li>";

                }

                echo '</ul>

			        </div>
		    </div>';
            } else { ///*******************Si el usuario es un cliente


                $c_menus = "SELECT DISTINCT id_menu_cat, imagen FROM (SELECT DISTINCT menus.id_menu_cat, menu, menus_cat.imagen, url, menus.estatus FROM menus
				INNER JOIN permisos_clientes ON menus.id_menu = permisos_clientes.id_menu
				INNER JOIN `menus_cat` ON menus.id_menu_cat=menus_cat.id_menu_cat
				WHERE permisos_clientes.id_usuario =" . $_SESSION['idUser'] . " ORDER BY menus.menu) AS consulta WHERE estatus = 1 ";
                $r_menus = $manager->ejecutarConsulta($c_menus);
                echo '<div class="container">

		    <div id="dl-menu" class="dl-menuwrapper">
				<ul class="dl-menu">';
                while ($a_menus = mysqli_fetch_array($r_menus)) {


                    $c_cat = "SELECT * FROM menus_cat WHERE id_menu_cat = '" . $a_menus['id_menu_cat'] . "' AND estatus = '1' ORDER BY id_menu_cat";
                    $r_cat = $manager->ejecutarConsulta($c_cat);
                    $a_cat = mysqli_fetch_array($r_cat);
                    if (mysqli_num_rows($r_cat) == 0) {
                    } else {
                        echo "<li>";
                        echo "<a href='#'><img src='../imagenes/icons/" . $a_menus['imagen'] . ".png' width='24'> " . $a_cat['categoria'] . "</a>";
                        echo "<ul class='dl-submenu'>";

                    }

                    $c_menus2 = "SELECT DISTINCT menus.id_menu_cat, menu, url, menus.id_menu, menus_cat.* FROM menus
                    INNER JOIN permisos_clientes ON menus.id_menu = permisos_clientes.id_menu
                    INNER JOIN menus_cat ON menus_cat.id_menu_cat = menus.id_menu_cat
                    WHERE menus.id_menu_cat = '" . $a_menus['id_menu_cat'] . "' AND menus.estatus = 1 AND
                        permisos_clientes.id_usuario = '" . $_SESSION['idUser'] . "' AND menus_cat.estatus = 1 ORDER BY menus_cat.id_menu_cat";

                    $r_menus2 = $manager->ejecutarConsulta($c_menus2);
                    while ($a_menus2 = mysqli_fetch_array($r_menus2)) {
                        ?>
                        <li><a href="#"
                               onClick="addTapMen('<?= $a_menus2['url']; ?>','<?= $a_cat['categoria'] . "-" . $a_menus2['menu']; ?>', '<?= $a_menus['imagen']; ?>');"><?= $a_menus2['menu']; ?></a>
                        </li>
                        <?
                        $i = $i + 1;
                    }
                    echo "</ul>";
                    echo "</li>";

                }

                echo '</ul>
			</div>
		</div>';

            }


            ?>


        </div>

        <!-- Sidebar end -->

    </div>




    <?php


    /* snow falling */
    
    /*
    if (date('m') == 12) {

        ?>
        <script src="..//plugins/jquery.snow.min.1.0.js"></script>
        <script>
            $.fn.snow();
        </script>

        <?
    }*/

    ?>


    </body>


    <script>

        let idUser = <?= $_SESSION['idUser'] ?>;


        if (localStorage.getItem('userid') === null) {
            localStorage.setItem('userid', idUser);
        } else {

            let userid = localStorage.getItem('userid');

            if (userid != idUser)
                localStorage.clear();


        }


    </script>


    </html>


    <?php

} else {

    echo 'No has iniciado sesion, redireccionando.....';

    header('refresh:2,../index.php');

}


?>