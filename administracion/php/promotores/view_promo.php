<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    $id_promo = $_REQUEST['idPromotor'];

    include_once('../../connexion/DataBase.php');

    $manager = DataBase::getInstance();

    $query_prom = "SELECT p.*,concat(s.nombreSupervisor,' ',s.apellidoSupervisor) AS nombreSuper, e.nombre AS estado_nom, t.descripcion AS tipoProm, p.fechaBaja 
      FROM Promotores p
      LEFT JOIN Supervisores AS s ON (s.idSupervisores=p.Supervisor)
      LEFT JOIN  estados e ON (e.id=p.idEstado)
      LEFT JOIN tipoPromotor t ON (t.idtipoPromotor=p.idtipoPromotor)
      WHERE idCelular='" . $id_promo . "'";

    $resul_prom = $manager->ejecutarConsulta($query_prom);

    $datos_prom = mysqli_fetch_array($resul_prom);

    $query_foto = "SELECT * FROM promotores_expediente WHERE idPromotor='" . $id_promo . "' AND id_documento='10'";

    $resul_foto = $manager->ejecutarConsulta($query_foto);

    $n_foto = mysqli_num_rows($resul_foto);

    ?>
    <div id="promotor_datos" class="easyui-accordion" style="width:100%;height:100%;">
        <div align="center" title="Datos Generales" data-options="iconCls:'icon-edit'" style="height: 100%;">

            <div class="cardview">

                <table width="100%">
                    <tr>
                        <td>
                            <input type="hidden" name="id_prom" id="id_prom" value="<? echo $id_promo; ?>">
                            <? //****Si tiene foto de perfil capturada
                            if ($n_foto > 0) {
                                $datos_foto = mysqli_fetch_array($resul_foto);

                                ?>
                                <div align="center">
                                    <img class="fine" src="<? echo $datos_foto['url']; ?>"
                                         style="max-width:200px;margin:20px;" exif="true"> <br><br>
                                </div>

                                <?


                            } //****Si no tiene foto de perfil tomada
                            else {
                                ?>
                                <div align="center">
                                    <img class="fine" src="../../imagenes/no_image.jpg"
                                         style="max-width:200px;margin:20px;" exif="true"> <br><br>
                                </div>


                            <? } ?>

                            <div class="card-face__name">

                                <?= $datos_prom['nombre']; ?>

                            </div>

                            <div class="card-face__title2" align="center">
                                <?= $datos_prom['tipoProm']; ?>
                            </div>

                            <div class="card-face__title1"> SUPERVISOR</div>
                            <div class="card-face__title2" align="center"><? echo $datos_prom['nombreSuper']; ?></div>


                        </td>

                        <td>
                            <table style="max-width: 80%;">
                                <tr>

                                    <td>
                                        <span class="card-face__title2">Rfc</span>
                                        <span class="card-face__name2"><?= $datos_prom['rfc']; ?> </span>
                                    </td>


                                    <td>
                                        <span class="card-face__title2">Curp</span>
                                        <span class="card-face__name2"><?= $datos_prom['curp']; ?> </span>
                                    </td>

                                </tr>
                                <tr>

                                    <td>
                                        <span class="card-face__title2">IMSS</span>
                                        <span class="card-face__name2"><?= $datos_prom['imss']; ?></span>
                                    </td>

                                    <td>
                                        <span class="card-face__title2">Estado</span>
                                        <span
                                                class="card-face__name2"><?= $datos_prom['estado_nom']; ?> </span>
                                    </td>


                                </tr>

                                <tr>
                                    <td>
                                        <span class="card-face__title2">Alta IMSS</span>
                                        <span class="card-face__name2"><?= $datos_prom['fechaAltaIMSS']; ?></span>
                                    </td>
                                    <td></td>

                                </tr>
                                <tr>
                                    <td>
                                        <span class="card-face__title2">USUARIO</span>
                                        <span class="card-face__name2"><?= $datos_prom['usuario']; ?></span>
                                    </td>


                                    <? if ($_SESSION['id_perfil'] == 1) { ?>
                                        <td>
                                            <span class="card-face__title2">Fecha alta Sistema</span>
                                            <span class="card-face__name2"><?= $datos_prom['fechaAlta']; ?></span>
                                        </td>
                                    <? } ?>


                                </tr>

                                <tr>

                                    <td>
                                        <span class="card-face__title2">celular personal</span>
                                        <span class="card-face__name2"><?= $datos_prom['numero_celular']; ?></span>
                                    </td>


                                    <td>
                                        <span class="card-face__title2">CELULAR VANGUARDIA</span>
                                        <span class="card-face__name2"><?= $datos_prom['cel_vanguardia']; ?></span>
                                    </td>


                                </tr>
                                <tr>

                                    <td>
                                        <span class="card-face__title2">IMEI</span>
                                        <span class="card-face__name2"><?= $datos_prom['imei']; ?></span>
                                    </td>


                                    <td>
                                        <span class="card-face__title2">EMAIL PERSONAL</span>
                                        <span class="card-face__name2"><?= $datos_prom['email']; ?> </span>
                                    </td>


                                </tr>

                                <tr>
                                    <td>
                                        <span class="card-face__title2">EMAIL VANGUARDIA</span>
                                        <span class="card-face__name2"><?= $datos_prom['email_vang']; ?></span>
                                    </td>

                                    <td>
                                        <span class="card-face__title2">NO. NOMINA</span>
                                        <span class="card-face__name2"><?= $datos_prom['no_nomina']; ?></span>
                                    </td>

                                </tr>
                                <tr>
                                    <td>
                                        <span class="card-face__title2">TALLA</span>
                                        <span class="card-face__name2"><?= $datos_prom['talla_playera']; ?></span>
                                    </td>

                                    <td>
                                        <span class="card-face__title2">NOMBRE EMERGENCIA</span>
                                        <span class="card-face__name2"><?= $datos_prom['nombre_emer']; ?></span>
                                    </td>

                                </tr>
                                <tr>
                                    <td>
                                        <span class="card-face__title2">TELEFONO EMERGENCIA</span>
                                        <span class="card-face__name2"><?= $datos_prom['tel_emer']; ?></span>
                                    </td>


                                    <td style="max-width: 150px;">
                                        <span class="card-face__title2">MARCAS</span>
                                        <span>
                                            <?
                                            $sql_marcas = "SELECT m.nombre, GROUP_CONCAT(m.`nombre` ORDER BY m.`nombre` SEPARATOR ' / ') marcas 
                                                                FROM marcaAsignadaPromotor map 
                                                                    INNER JOIN Marca m ON (map.idMarca=m.idMarca)
                                                                    WHERE idPromotor = $id_promo
                                                                GROUP BY map.`idPromotor` ";

                                            $rs_marcas = $manager->ejecutarConsulta($sql_marcas);

                                            $resul = mysqli_fetch_assoc($rs_marcas);

                                            echo $resul['marcas'];


                                            ?>
                                       </span>


                                    </td>


                                </tr>


                                <tr>

                                    <td>

                                    </td>

                                    <td>
                                        <span class="card-face__title2">Fecha Baja</span>
                                        <span class="card-face__name2"><?= $datos_prom['fechaBaja'] ?></span>

                                    </td>


                                </tr>
                            </table>
                        </td>
                    </tr>

                </table>

            </div>
        </div><!-- Termina el primer div del acordeon-->

        <? if ($_SESSION['id_perfil'] == 1) {
            ?>
            <div align="center" title="Expediente" data-options="iconCls:'icon-pdf'">

                <div id="expediente_prom" style="height:100%">
                    <div style="background:#fafafa; color:#222; padding:10px;">
                        <table style="width:95%;height:100%">
                            <?
                            $sql_docs = "SELECT * FROM expediente_docs WHERE estatus='1' ORDER BY id_documento";
                            $rs_docs = $manager->ejecutarConsulta($sql_docs);
                            $k = 0;
                            while ($dat_docs = mysqli_fetch_array($rs_docs)) {

                                $sql_exp = "SELECT * FROM promotores_expediente WHERE idPromotor='" . $id_promo . "'
					                            AND id_documento='" . $dat_docs['id_documento'] . "' AND estatus='1'";
                                $rs_exp = $manager->ejecutarConsulta($sql_exp);
                                $dat_exp = mysqli_fetch_array($rs_exp);

                                $check = "";
                                if ($dat_exp) {
                                    $check = "checked disabled";
                                }

                                if (($k % 2) == 0) {
                                    ?>
                                    <tr>

                                <? } ?>
                                <td>

                                    <input id="doc<? echo $dat_docs['id_documento']; ?>"
                                           name="doc<? echo $dat_docs['id_documento']; ?>" type="checkbox"
                                           value="<? echo $dat_docs['id_documento']; ?>" <? echo $check; ?>
                                           onChange="<? if ($dat_docs['id_documento'] != 10) { ?>window_file(<?= $dat_docs['id_documento'];?>);<? } else { ?>window_pic(<?= $dat_docs['id_documento'];?>);<? } ?>"
                                           class="css-checkbox"/>
                                    <label for="doc<? echo $dat_docs['id_documento']; ?>"
                                           class="css-label"> <?= $dat_docs['documento']; ?></label>


                                    <?
                                    if ($dat_exp) {
                                        ?>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <a target="_blank"
                                           href="../php/promotores/down_doc.php?file=<?= $dat_exp['id_expediente']; ?>"
                                           class="easyui-linkbutton" iconCls="icon-pdf"></a>
                                        <? if ($dat_docs['id_documento'] != 10) {
                                            ?>
                                            <a target="_blank"
                                               href="../php/promotores/view_doc.php?file=<?= $dat_exp['id_expediente']; ?>"
                                               class="easyui-linkbutton" iconCls="icon-print"></a>
                                        <? }
                                    } ?>

                                </td>

                                <? $k++;
                                if (($k % 2) == 0) {
                                    ?>
                                    </tr>
                                    <?
                                }
                            } ?>


                        </table>
                    </div>
                </div>

            </div><!-- Termina el tercer div del acordeon-->
        <? } ?>
    </div>
    <script type="text/javascript">
        function ver_desemp() {
            let idSem = $('#SemanaLab').combobox('getValue');

            $.ajax({
                beforeSend: function () {
                    $("#desemp_lab").html('');
                    $("<img src='../imagenes/loading.gif' alt='loading' style='position:relative;top:0px;left:50px;z-index:2000' id='loading-imagen' />").appendTo("#promotores");
                },
                complete: function () {
                    $("#loading-imagen").remove();
                },
                type: 'POST',
                url: '../php/promotores/queryDesemp.php',
                data: {idSem: idSem},

                error: function (jqXHR, textStatus, error) {

                    $.messager.alert('Promotores', "error: " + jqXHR.responseText, 'error');
                }
            }).done(function (data) {
                $("#desemp_lab").html(data);
                porcentaje_vis();

            });

        }

        $(document).ready(function (e) {

            let id_prom = document.getElementById('id_prom').value;


            $('#SemanaLab').combobox({

                valueField: 'idSupervision',

                textField: 'semana_lab',

                url: '../php/get_semanaLab.php?id_prom=' + id_prom,

                onSelect: function (record) {

                    ver_desemp();
                }

            });

            let $on = 'section';
            $($on).css({
                'background': 'none',
                'border': 'none',
                'box-shadow': 'none'
            });

        });

        function window_file(idDoc) {
            let id_prom = document.getElementById('id_prom').value;

            document.getElementById('dlg_doc').style.display = '';
            $('#dlg_doc').dialog({

                title: 'Subir Archivo',
                href: '../php/promotores/subir_doc.php?idDoc=' + idDoc + '&idProm=' + id_prom,
                width: 400,
                height: 200,
                buttons: [{
                    text: 'Ok',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#dlg_doc').dialog('close');
                    }
                }]

            });
        }

        function window_pic(idDoc) {
            let id_prom = document.getElementById('id_prom').value;

            document.getElementById('dlg_doc').style.display = '';
            $('#dlg_doc').dialog({

                title: 'Subir Fotografia',
                href: '../php/promotores/subir_pic.php?idDoc=' + idDoc + '&idProm=' + id_prom,
                width: 400,
                height: 200,
                buttons: [{
                    text: 'Ok',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#dlg_doc').dialog('close');
                    }
                }]

            });
        }

    </script>
    <div id="dlg_doc" style="display:none; padding:10px 20px;"></div>


    <?
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>