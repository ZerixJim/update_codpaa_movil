<?php

/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 29/07/14
 * Time: 16:35
 */

ob_start();
session_start();

if ($_SESSION['permiso'] >= 1):
include_once('../php/seguridad.php');
?>

<div id="promotores" class="content-padding">


    <div style="display: flex;justify-content: space-between;padding: 15px;">

        <div></div>

        <div style="display: flex;">

            <input id="search_prom" style="width:150px; border: 1px solid #ccc;padding: 10px;border-right: none;" type="text"
                   onKeyPress="promEnter(event);" placeholder="buscar..">
            <a href="#" class="easyui-linkbutton" id="btn-search-prom"  onClick="searchProm();"
               style="padding: 5px;background-color: #0D98FB;color: #FFFFFF;border-radius: 0 5px 5px 0;">Buscar</a>

        </div>
    </div>



    <table id="dg_prom" class="easyui-datagrid" url="../php/promotores/get_prom.php"
           toolbar="#toolbarPr" fitColumns="true" singleSelect="true"
           idField="idCelular" sortName="idCelular" sortOrder="asc"
           style="max-height:600px;min-height: 550px;"
           data-options="method: 'post', view:bufferview,remoteSort:false,multiSort:true,
				onDblClickRow: onDblClickRowProm, onClickRow:btnsEnabled, fit:true, onClickCell:incapacidad">

        <thead>

        <tr>

            <th field="idCelular" width="5" sortable="true" sorter="mysort">Cartera</th>
            
            <th data-options="field:'nombreSupervisor'" width="15" sortable="true">Supervisor</th>
            
            <th field="Ruta" width="5" sortable="true">Ruta</th>

            <th field="nombre" width="20" sortable="true">Nombre</th>
            <th field="status" width="15"  formatter="status" align="center"></th>

            <th field="usuario" width="10" sortable="true">Usuario</th>

            <th field="password" width="10">Contraseña</th>

            <th field="numero_celular" width="10">No. Celular</th>

            <th field="Estadomx" width="10" sortable="true">Estado</th>
        </tr>

        </thead>


    </table>

    <!-- toolbar promotores-->


    <div id="toolbarPr" style="padding: 5px;">
        <? if ($_SESSION['permiso'] >= 3) {
            /*solo los administradores y rh pueden crear promotores*/
            if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '10' || $_SESSION['id_perfil'] == '5') {
                ?>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                   onclick="newPromo()">Nuevo Promotor</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                   onclick="openIcapacidadDialog()">Incapacidad</a>


            <? } ?>
            <a href="javascript:void(0)" id="btn-editar-promotores" style="display: none" class="easyui-linkbutton"
               iconCls="icon-edit" plain="true"
               onclick="editProm()">Editar Promotor</a>

            <? if ($_SESSION['id_perfil'] == 1   || $_SESSION['id_perfil'] == 10) :
                ?>
                <a href="javascript:void(0)" id="btn-remover-promotores" style="display: none;"
                   class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                   onclick="removeProm()">Baja Promotor</a>

            <? endif;
        }
        ?>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-map" plain="true"
           onclick="rutaProm()">Ver Rutas</a>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true"
           onclick="searchProm()">Refrescar tabla</a>


    </div>

    <!-- dialogo Promotores-->


    <div id="dlg_prom" title="Infomacion de Promotor" class="easyui-dialog"
         style="width:50%;height:80%;padding:30px 20px" closed="true" buttons="#dlg-buttons"
         data-options="modal:true">


        <form id="fm_promnew" method="post" class="form-style">


            <ul>
                <li>

                    <input name="nombre" id="nombre" class="easyui-textbox"
                           data-option="required:true" prompt="Nombre" required>
                </li>
                <li>

                    <input class="easyui-combobox" name="nombreSupervisor" id="nombreSupervisor"
                           data-options="valueField:'idSupervisores',textField:'nombreSupervisor',url:'../php/get_supervisor.php',required:true"
                           prompt="supervisor"
                           required>
                </li>
                <li>

                    <input class="easyui-combobox" name="tipo_promotor" id="tipo_promotor"
                           data-options="valueField:'idtipoPromotor',textField:'descripcion',url:'../php/fotos/getTipoProm.php',required:true"
                           prompt="tipo promotor"
                           required>
                </li>
                <li>


                    <input name="rfc" id="rfc" class="easyui-textbox" prompt="Rfc" required>
                </li>
                <li>


                    <input name="curp" id="curp" class="easyui-textbox" prompt="curp" required>
                </li>
                <li>


                    <input name="imss" id="imss" class="easyui-textbox" prompt="IMSS" >

                    <input class="easyui-datebox" id="fechaAltaImss" name="fechaAltaImss"
                           data-options="formatter:myformatter,parser:myparser" prompt="Fecha Alta Imss" required>
                </li>



                <li>


                    <input class="easyui-combobox" name="id_estado" id="id_estado"
                           data-options="valueField:'id',textField:'nombre',url:'../php/get_estado.php',required:true"
                           prompt="Estado"
                           required>
                </li>
                <li>


                    <input maxlength="10" name="usuario" id="usuario"
                           style="border: 1px solid #ccc; border-radius: 4px;padding: 3px;"
                           onKeyUp="checkUser();" required class="easyui-validatebox">
                    <span id="chk_us" style="color:#aafe86;"></span>


                </li>
                <li>


                    <input name="password" id="password" class="easyui-textbox"
                           data-option="required:true" prompt="contraseña" required>
                    <a id="random_pass" href="#" name="random_pass" class="easyui-linkbutton" data-options="plain:true" onClick="randomPass()">Generar contraseña</a>


                </li>

                <li>

                    <input name="no_cel" id="no_cel" class="easyui-textbox" prompt="Celular Personal" required>
                </li>
                <li>


                    <input name="cel_vang" id="cel_vang" class="easyui-textbox" prompt="Celular Vanguardia">
                </li>
                <li>


                    <input name="imei" id="imei" class="easyui-textbox" prompt="imei">
                </li>
                <li>

                    <input name="emailProm" id="emailProm" class="easyui-textbox" prompt="email personal" required>
                </li>
                <li>


                    <input name="emailVang" id="emailVang" class="easyui-textbox" prompt="email vanguardia">
                </li>
                <li>


                    <input name="no_nomina" id="no_nomina" class="easyui-textbox" prompt="No. nomina" required>
                </li>
                <li>

                    <select class="easyui-combobox" name="talla_play" id="talla_play" style="width: 80px;" prompt="talla">
                        <option value="CH">CH</option>
                        <option value="M">M</option>
                        <option value="G">G</option>
                        <option value="XL">XL</option>
                    </select>

                </li>
                <li>

                    <input name="nombre_emer" id="nombre_emer" class="easyui-textbox" prompt="contacto emergencia"
                           style=" width:250px" required>
                </li>
                <li>

                    <input name="tel_emer" id="tel_emer" class="easyui-textbox" prompt="telefono emergencia" required>
                </li>

            </ul>


        </form>


    </div>


    <div id="dlg-buttons">

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
           onclick="saveProm()">Guardar</a>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="hideDialogPr()">Cancelar</a>

    </div>

    <div id="dlg_viewPm" style="padding:10px 20px;display: none;">
    </div>
    <div id="dlg_editPm" style="padding:10px 20px;display: none;">
    </div>
    <div id="dlg_rutaPm" style="padding:10px 20px;display: none;">
    </div>
    <div id="dlg_removePM" style="padding:10px 20px;display:none;">
        <span><br><br>&nbsp;&nbsp;Estas seguro de eliminar el Promotor?</span>
    </div>


    <div id="dlg_incapacidad" class="easyui-dialog"  data-options="border:'thin', modal:true, closed:true, title:'Incapacidad'">

        <form action="" class="form-style">

            <ul>
                <li>
                    <label for="f-inicio">Fecha inicio</label>
                    <input type="text" id="f-inicio" class="easyui-datebox" placeholder="de">
                </li>
                <li>
                    <label for="f-fin">Fecha final</label>
                    <input type="text" id="f-fin" class="easyui-datebox" placeholder=" a ">
                </li>
                <li>

                </li>
            </ul>

        </form>


    </div>


    <script >
        let map_ruta;
        let markers_ruta = [];


        function openIcapacidadDialog(){

            $('#dlg_incapacidad').dialog('open');


        }




        //*****************************************Funciones Promotores *************************************//
        function newPromo() {

            $('#dlg_viewPm').show();

            $('#dlg_prom').dialog('open').dialog('setTitle', 'Nuevo Promotor');

            $('#fm_promnew').form('clear');

            // url = '../php/save_user.php';

        }

        function saveProm() {
            let nombre = $('#nombre').val();
            let supervisor = $('#nombreSupervisor').combobox('getValue');
            let tipo_promotor = $('#tipo_promotor').combobox('getValue');
            let id_estado = $('#id_estado').combobox('getValue');
            let usuario = $('#usuario').val();
            let pass = $('#password').val();
            let no_cel = $('#no_cel').val();
            let imei = $('#imei').val();
            let email = $('#emailProm').val();
            let no_nomina = $('#no_nomina').val();
            let talla_p = $('#talla_play').combobox('getValue');
            let nombre_emer = $('#nombre_emer').val();
            let tel_emer = $('#tel_emer').val();
            let cel_vang = $('#cel_vang').val();
            let emailVang = $('#emailVang').val();
            let rfc = $('#rfc').val();
            let curp = $('#curp').val();
            let imss = $('#imss').val();
            let fechaImss = $('#fechaAltaImss').val();

            let isValid = $('#fm_promnew').form('validate');

            if (isValid) {
                $.ajax({
                    type: 'POST',
                    url: '../php/promotores/save_prom.php',
                    data: {
                        nombre: nombre,
                        supervisor: supervisor,
                        tipo_promotor: tipo_promotor,
                        id_estado: id_estado,
                        usuario: usuario,
                        pass: pass,
                        no_cel: no_cel,
                        imei: imei,
                        email: email,
                        no_nomina: no_nomina,
                        talla_p: talla_p,
                        nombre_emer: nombre_emer,
                        tel_emer: tel_emer,
                        cel_vang: cel_vang,
                        emailVang: emailVang,
                        rfc: rfc,
                        curp: curp,
                        imss: imss,
                        fechaAltaImss: fechaImss
                    },

                    success: function (data) {
                        if (data === '1') {
                            $.messager.alert('Promotores', 'Promotor Registrado Con Exito');
                            $('#dlg_prom').dialog('close');
                            $('#dg_prom').datagrid('reload');
                        } else if (data === '0') {
                            $.messager.alert('Promotores', 'Ocurrio un Problema', 'error');
                        }
                    },

                    error: function (jqXHR, textStatus, error) {

                        alert("error: " + jqXHR.responseText);
                    }
                });
            } else {

                $.messager.alert('Aviso', 'Faltan datos', 'warning');


            }
        }

        function saveEditPromotor(idProm) {
            let nombre = $('#nombreE').val();
            let supervisor = $('#nombreSupervisorE').combobox('getValue');
            let tipo_promotor = $('#tipo_promotorE').combobox('getValue');
            let id_estado = $('#id_estadoE').combobox('getValue');
            let usuario = $('#usuarioE').val();
            let pass = $('#passwordE').val();
            let no_cel = $('#no_celE').val();
            let imei = $('#imeiE').val();
            let email = $('#emailPromE').val();
            let no_nomina = $('#no_nominaE').val();
            let talla_p = $('#talla_playE').combobox('getValue');
            let nombre_emer = $('#nombre_emerE').val();
            let tel_emer = $('#tel_emerE').val();
            let cel_vang = $('#cel_vangE').val();
            let emailVang = $('#emailVangE').val();
            let rfc = $('#rfcE').val();
            let curp = $('#curpE').val();
            let imss = $('#imssE').val();
            let fechaImss = $('#altaImss').datebox('getValue');


            let isValid = $('#fm_view').form('validate');

            if (isValid) {

                $.ajax({
                    type: 'POST',
                    url: '../php/promotores/save_edit_prom.php',
                    data: {
                        nombre: nombre,
                        supervisor: supervisor,
                        tipo_promotor: tipo_promotor,
                        id_estado: id_estado,
                        usuario: usuario,
                        pass: pass,
                        no_cel: no_cel,
                        imei: imei,
                        email: email,
                        no_nomina: no_nomina,
                        talla_p: talla_p,
                        nombre_emer: nombre_emer,
                        tel_emer: tel_emer,
                        cel_vang: cel_vang,
                        emailVang: emailVang,
                        rfc: rfc,
                        curp: curp,
                        imss: imss,
                        idPromotor: idProm,
                        fechaAltaImss: fechaImss
                    },

                    success: function (data) {
                        if (data === '1') {
                            $.messager.alert('Promotores', 'Promotor Editado Con Exito');
                            $('#dlg_editPm').dialog('close');
                            $('#dg_prom').datagrid('reload');
                        } else if (data === '0') {
                            $.messager.alert('Promotores', 'Ocurrio un Problema', 'error');
                        }
                    },

                    error: function (jqXHR, textStatus, error) {

                        alert("error: " + jqXHR.responseText);
                    }
                });
            } else {

                $.messager.alert('Aviso', 'Faltan datos', 'warning');

            }


        }

        function removePromo(idPromo) {
            let fechaBaja = $('#fechaBaja').datebox('getValue');
            $.ajax({

                type: 'POST',
                url: '../php/promotores/remove_prom.php',
                data: {idPromo: idPromo, fechaBaja: fechaBaja},

                success: function (data) {
                    if (data === '1') {

                        $.messager.alert('Promotores', 'Promotor Eliminado Con Exito');
                        $('#dlg_removePM').dialog('close');
                        $('#dg_prom').datagrid('reload');
                    } else if (data === '0') {
                        $.messager.alert('Promotores', 'Ocurrio un Problema', 'error');
                    }
                },

                error: function (jqXHR, textStatus, error) {

                    alert("error: " + jqXHR.responseText);
                }
            });


        }

        function searchProm() {
            let buscar = $('#search_prom').val();
            if (buscar === "") {
                buscar = '*';
            }


            if (buscar !== "") {
                $.ajax({
                    type: 'POST',
                    url: '../php/promotores/get_prom.php',
                    data: {buscarProm: buscar},

                    beforeSend: function () {

                        $('#btn-search-prom').linkbutton('disable');

                    },

                    success: function (data) {


                        $('#dg_prom').datagrid({
                            data: jQuery.parseJSON(data)
                        });

                        $('#btn-search-prom').linkbutton('enable');

                    },

                    error: function (jqXHR, textStatus, error) {

                        alert("error: " + jqXHR.responseText);

                        $('#btn-search-prom').linkbutton('enable');
                    }
                });
            } else {
                $('#dg_prom').datagrid('reload');
            }

        }

        function promEnter(e) {

            let key = e.keyCode || e.which;


            if (key === 13) {
                searchProm();
            }

        }

        ////***********************************Final Funciones Promotores*****************************************//


        function mysort(a, b) {
            a = parseInt(a);
            b = parseInt(b);
            return (a > b ? 1 : -1);
        }

        function hideDialogPr() {
            $('#dlg_prom').dialog('close');
            $('#dlg_viewPm').hide();
            $('#dlg_editPm').hide();
            $('#dlg_rutaPm').hide();
            $('#dlg_removePM').hide();

        }


        function onDblClickRowProm(index) {

            let idPromotor = $('#dg_prom').datagrid('getRows')[index]['idCelular'];
            $('#dlg_viewPm').show();

            $('#dlg_viewPm').dialog({
                title: 'Datos Promotor',
                href: '../php/promotores/view_promo.php?idPromotor=' + idPromotor,
                width: '80%',
                height: 650,
                center:true,
                buttons: [{
                    text: 'Ok',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#dlg_viewPm').dialog('close');
                    }
                }],
                modal: true
            });

        }

        function editProm() {
            let row = $('#dg_prom').datagrid('getSelected');
            let idProm = row.idCelular;

            let dialodEditProm = $('#dlg_editPm');

            dialodEditProm.show();

            dialodEditProm.dialog({
                title: 'Editar Promotor',
                href: '../php/promotores/edit_promo.php?idPromotor=' + idProm,
                width: 700,
                height: 600,
                buttons: [{
                    text: 'Ok',
                    iconCls: 'icon-ok',
                    handler: function () {
                        saveEditPromotor(idProm);
                    }
                }, {
                    text: 'Cancel',
                    handler: function () {
                        $('#dlg_editPm').dialog('close');
                    }
                }],
                modal: true
            });
        }

        function removeProm() {
            let row = $('#dg_prom').datagrid('getSelected');
            let idPromo = row.idCelular;


            document.getElementById('dlg_removePM').style.display = '';
            $('#dlg_removePM').dialog({
                title: 'Eliminar Promotor',
                href: '../php/promotores/elim_prom.php?idPromotor=' + idPromo,
                width: 600,
                height: 400,
                modal:true,
                center:true,
                buttons: [{
                    text: 'Ok',
                    iconCls: 'icon-ok',
                    handler: function () {
                        removePromo(idPromo);
                    }
                }, {
                    text: 'Cancel',
                    handler: function () {
                        $('#dlg_removePM').dialog('close');
                    }
                }]
            });
        }

        function rutaProm() {
            let row = $('#dg_prom').datagrid('getSelected');
            let idPromo = row.idCelular;

            document.getElementById('dlg_rutaPm').style.display = '';
            $('#dlg_rutaPm').dialog({
                title: 'Ver Ruta',
                href: '../php/promotores/ver_ruta.php?idPromotor=' + idPromo,
                width: 850,
                height: 650,
                buttons: [{
                    text: 'Ok',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#dlg_rutaPm').dialog('close');
                    }
                }],
                modal: true
            });
        }

        function ruta_tiendas(tiendas_rut) {

            let center_x, center_y;
            let directionsDisplay1 = new google.maps.DirectionsRenderer();

            let mapOptions = {
                zoom: 5,

                center: new google.maps.LatLng(22.35006135229113, -101.55759218749995),

                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            let map_ruta = new google.maps.Map(document.getElementById('Mapa-Ruta'), mapOptions);

            directionsDisplay1.setMap(map_ruta);

            for (var i = 0; i < tiendas_rut.length; i++) {

                var tien_rut = tiendas_rut[i];

                var sucur = tien_rut.sucursal;

                //***********************
                var msj = '<div id="content">' +
                    '<h2 id="firstHeading" class="firstHeading">Sucursal</h2>' +
                    '<div id="bodyContent">' +
                    '<p>' + tien_rut.formato + '</p>' +
                    '<p>' + tien_rut.direcc + '</p>' +
                    '</div>' +
                    '</div>';


                var myLatLngTiendas = new google.maps.LatLng(tien_rut.x, tien_rut.y);

                markerTiendas = new google.maps.Marker({

                    position: myLatLngTiendas,

                    map: map_ruta,

                    icon: '../images/formatos/' + tien_rut.idFormato + '.png',

                    title: sucur


                });
                markers_ruta.push(markerTiendas);
                messageStore(markerTiendas, msj);

                center_x = tien_rut.x;

                center_y = tien_rut.y;
            }
            var centerLT = new google.maps.LatLng(center_x, center_y);
            map_ruta.setCenter(centerLT);
            map_ruta.setZoom(12);
        }


        // Sets the map on all markers in the array.
        function setAllMap(map) {
            for (let i = 0; i < markers_ruta.length; i++) {
                markers_ruta[i].setMap(map);
            }
        }

        // Removes the markers from the map, but keeps them in the array.
        function clearMarkers() {
            setAllMap(null);
        }

        // Deletes all markers in the array by removing references to them.
        function deleteMarkers() {
            clearMarkers();
            markers_ruta = [];
        }

        function checkUser() {
            let user_ch = $('#usuario').val();

            $.ajax({
                type: 'POST',
                url: '../php/promotores/check_user.php',
                data: {user_ch: user_ch},

                error: function (jqXHR, textStatus, error) {

                    $.messager.alert('Promotores', "error: " + jqXHR.responseText, 'error');
                }
            }).done(function (data) {

                let user = $('#chk_us');


                if (data) {
                    user.html('No Disponible').css("color", "red");

                } else {
                    user.html('Disponble').css("color", "green");
                }

            });

        }

        function checkUserE() {
            let user_ch = $('#usuarioE').val();

            $.ajax({
                type: 'POST',
                url: '../php/promotores/check_user.php',
                data: {user_ch: user_ch},

                error: function (jqXHR, textStatus, error) {

                    $.messager.alert('Promotores', "error: " + jqXHR.responseText, 'error');
                }
            }).done(function (data) {
                if (data) {
                    $('#chk_usE').html('Nombre de Usuario No Disponible');
                } else {
                    $('#chk_usE').html('Nombre de Usuario Disponble');
                }

            });

        }

        function randomPass() {
            let randomstring = Math.random().toString(36).slice(-8);
            //$('#password').val(randomstring)
            $('#password').textbox('setValue', randomstring);

        }


        function myformatter(date) {
            let y = date.getFullYear();
            let m = date.getMonth() + 1;
            let d = date.getDate();
            return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
        }

        function myparser(s) {
            if (!s) return new Date();
            let ss = (s.split('-'));
            let y = parseInt(ss[0], 10);
            let m = parseInt(ss[1], 10);
            let d = parseInt(ss[2], 10);
            if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
                return new Date(y, m - 1, d);
            } else {
                return new Date();
            }
        }

        function btnsEnabled() {
            let row = $('#dg_prom').datagrid('getSelected');
            if (row) {
                $('#btn-remover-promotores').css("display", "inline-block");
                $('#btn-editar-promotores').css("display", "inline-block");
            } else {
                $('#btn-editar-promotores').css("display", "none");
                $('#btn-remover-promotores').css("display", "none");
            }
        }


        function status(val, row){

            if (row.status === 'b'){

                return `<span  class='span-click-cell' style='padding: 5px;background-color: #f84545;border-radius: 8px;color: #f3f3f3;font-weight: bold;'>Baja</span>`;

            } else {
                
                return `<span style='padding: 5px;background-color: #52ff73;border-radius: 8px;color: #f3f3f3;font-weight: bold;'>Activo</span>`;

                /*if (row.inca === null){

                    return `<span style='padding: 5px;background-color: #52ff73;border-radius: 8px;color: #f3f3f3;font-weight: bold;'>Activo</span>`;

                }else {
                    return `<span class='span-click-cell' style='padding: 5px;background-color: #fff55a;border-radius: 8px;color: #f3f3f3;font-weight: bold;text-shadow: 0 0 2px #666666;'>Incapacidad</span>`;
                }*/

            }

        }

        function incapacidad(index,field,value){
            let row = $(this).datagrid('getRows')[index];
            if (field === 'status'){

                if (row.inca !== null){

                    alert('Incapacidad \n' + row.inca);

                }


            } else {

            }
        }
    </script>


    <style>

        .span-click-cell:hover{

            cursor: pointer;

        }

    </style>


    <?
    endif; ?>

    <!-- FIN PROMOTORES-->


</div>

