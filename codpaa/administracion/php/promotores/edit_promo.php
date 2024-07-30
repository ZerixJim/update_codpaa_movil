<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    ?>
    <html>
    <head>

    </head>

    <body>
    <?
    $id_promo = $_REQUEST['idPromotor'];

    include_once('../../connexion/DataBase.php');
    include_once('../../php/seguridad.php');
    $manager = DataBase::getInstance();

    $query_prom = "SELECT * FROM Promotores p WHERE idCelular='" . $id_promo . "'";

    $resul_prom = $manager->ejecutarConsulta($query_prom);

    $datos_prom = mysqli_fetch_array($resul_prom);

    $query_foto = "SELECT * FROM photoCatalogo WHERE id_promotor='" . $id_promo . "' AND id_marca='35' ORDER BY idphotoCatalogo DESC";

    $resul_foto = $manager->ejecutarConsulta($query_foto);

    $n_foto = mysqli_num_rows($resul_foto);

    ?>
    <div align="center">
        <form id="fm_view" method="post">


            <ul>
                <li>
                    <?php
                    if ($n_foto > 0) {
                        $datos_foto = mysqli_fetch_array($resul_foto);
                        try {
                            $image = new Imagick('http://www.plataformavanguardia.net/' . $datos_foto['imagen']);
                            $orientation = $image->getImageOrientation();
                        } catch (Exception $e) {
                            $orientation = 0;
                        }

                        ?>
                        <img class="<? if ($orientation == '6') {
                            echo "rotate-right";
                        } else {
                            echo "fine";
                        } ?>" src="http://www.plataformavanguardia.net/<? echo $datos_foto['imagen']; ?>"
                             style="max-width:250px;margin:20px;" exif="true"> <br><br>
                    <? } else { ?>
                        <img src="../../imagenes/no_image.jpg" style="max-width:300px;margin:20px;" exif="true">
                        <br><br>


                    <? } ?>
                </li>
                <li>
                    <label for="nombreE">Nombre</label>

                    <input name="nombreE" id="nombreE" class="easyui-textbox"

                           data-options="value:'<?= $datos_prom['nombre']; ?>'" <? if ($_SESSION['id_perfil'] == '2') {
                        echo "readonly";
                    } ?>

                </li>
                <li>

                    <label for="nombreSupervisorE">Supervisor</label>
                    <input class="easyui-combobox" name="nombreSupervisorE"
                           id="nombreSupervisorE"
                           data-options="valueField:'idSupervisores',textField:'nombreSupervisor',url:'../php/get_supervisor.php',value:'<?= $datos_prom['Supervisor'];?>'"
                           required>

                </li>
                <li>
                    <label for="tipo_promotorE">Tipo promotor</label>
                    <input class="easyui-combobox" name="tipo_promotorE" id="tipo_promotorE"
                           data-options="valueField:'idtipoPromotor',textField:'descripcion',url:'../php/fotos/getTipoProm.php', value:'<?= $datos_prom['idtipoPromotor'];?>'"
                           required>

                </li>
                <li>
                    <label for="rfcE">RFC</label>
                    <input name="rfcE" id="rfcE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['rfc']; ?>'" required>

                </li>
                <li>
                    <label for="curpE"><strong>Curp </strong></label>
                    <input name="curpE" id="curpE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['curp']; ?>'">

                </li>
                <li>
                    <label for="imssE">IMSS </label>
                    <input name="imssE" id="imssE" class="easyui-textbox"

                           data-options="value:'<?= $datos_prom['imss']; ?>'">

                </li>
                <li>
                    <label for="altaImss">Fecha Alta IMSS</label>
                    <input id="altaImss" name="altaImss" class="easyui-datebox"
                           data-options="formatter:myformatter,parser:myparser,value:'<?= $datos_prom['fechaAltaIMSS']; ?>'">

                </li>
                <li>
                    <label for="id_estadoE">Estado</label>
                    <input class="easyui-combobox" name="id_estadoE" id="id_estadoE"
                           data-options="valueField:'id',textField:'nombre',url:'../php/get_estado.php'"
                           required>

                </li>
                <li>
                    <label for="usuarioE">Usuario</label>
                    <input name="usuarioE" id="usuarioE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['usuario']; ?>'">
                    <span id="chk_usE" style="color:#64FE2E;"></span>

                </li>
                <li>
                    <label for="passwordE">Contraseña</label>
                    <input name="passwordE" id="passwordE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['password']; ?>'" required>

                </li>
                <li>
                    <label for="no_celE">Celular personal</label>
                    <input name="no_celE" id="no_celE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['numero_celular']; ?>'">

                </li>
                <li>
                    <label for="cel_vangE">Celular vanguardia</label>
                    <input name="cel_vangE" id="cel_vangE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['cel_vanguardia']; ?>'">

                </li>
                <li>
                    <label for="imeiE">Imei</label>
                    <input name="imeiE" id="imeiE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['imei']; ?>'">

                </li>
                <li>
                    <label for="emailPromE">Email personal</label>
                    <input name="emailPromE" id="emailPromE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['email']; ?>'">

                </li>
                <li>
                    <label for="emailVangE">Email vanguardia</label>
                    <input name="emailVangE" id="emailVangE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['email_vang']; ?>'">

                </li>
                <li>
                    <label for="no_nominaE">No. nomina</label>
                    <input name="no_nominaE" id="no_nominaE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['no_nomina']; ?>'">

                </li>
                <li>
                    <label for="talla_playE">Talla</label>
                    <select class="easyui-combobox" name="talla_playE" id="talla_playE">
                        <option <? if ($datos_prom['talla_playera'] == 'CH') {
                            echo "selected";
                        } ?> value="CH">CH
                        </option>
                        <option <? if ($datos_prom['talla_playera'] == 'M') {
                            echo "selected";
                        } ?> value="M">M
                        </option>
                        <option <? if ($datos_prom['talla_playera'] == 'G') {
                            echo "selected";
                        } ?> value="G">G
                        </option>
                        <option <? if ($datos_prom['talla_playera'] == 'XL') {
                            echo "selected";
                        } ?> value="XL">XL
                        </option>
                    </select>

                </li>
                <li>
                    <label for="nombre_emerE">Contacto emergencia</label>

                    <input name="nombre_emerE" id="nombre_emerE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['nombre_emer']; ?>'">

                </li>
                <li>
                    <label for="tel_emerE">Telefono emergencia</label>
                    <input name="tel_emerE" id="tel_emerE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['tel_emer']; ?>'">

                </li>
            </ul>


        </form>
    </div>
    <script language="javascript">
        $('#usuarioE').textbox({
            onChange: function () {
                checkUserE();
            }

        });


        $('#id_estadoE').combobox({

            onLoadSuccess: function (param) {
                <? if($datos_prom['idEstado'] != "" && $datos_prom['idEstado'] != NULL){?>
                $(this).combobox('select', '<?= $datos_prom['idEstado'];?>');

                <? }?>
            }

        });


    </script>
    </body>
    </html>

    <?
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>