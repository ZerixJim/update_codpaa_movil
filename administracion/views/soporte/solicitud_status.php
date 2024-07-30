<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 26/04/2017
 * Time: 12:47 PM
 */


ob_start();
session_start();


if (isset($_SESSION['idUser'])) {


    ?>


    <div id="content-solicitudes" class="content-padding"  style="height: 100%;" >



       <!-- <div class="filter-card">
            <svg xmlns="http://www.w3.org/2000/svg" onclick="actionFilter()" height="24" viewBox="0 0 24 24" width="24"><path d="M0 0h24v24H0V0z" fill="none"/><path d="M10 18h4v-2h-4v2zM3 6v2h18V6H3zm3 7h12v-2H6v2z"/></svg>

            <div class="content-filter">


            </div>
        </div>-->




        <table id="data-solicitud"

               style="height:600px;min-height: 550px;width: 100%;max-height: 600px;" toolbar="#tools">
            <thead>
            <tr>
                <th field="idSolicitud" data-options="align:'center'">[#Folio]</th>
                <th field="titulo" width="5">Titulo</th>
                <th field="attach" formatter="getAttach" width="2"></th>

                <th field="fechaPropuesta" data-options="align:'center'" width="5"> Fecha Entrega </th>
                <th field="createdAt" width="5" sortable="true">Fecha Creado</th>
                <th field="fechaAprobada" data-options="align:'center'" width="5">Fecha Aprobada</th>
                <th field="tipo" sortable="true" width="5">Tipo Solicitud</th>

                <?php if ($_SESSION['id_perfil'] == '1'):  ?>
                <th field="nombre" sortable="true" width="5">Usuario</th>
                <?php endif;?>

                <th field="status" width="5" data-options="align:'center'" formatter="actionFormatter">Estatus</th>


            </tr>
            </thead>
        </table>


        <div id="tools">

            <a href="#" class="easyui-linkbutton" onclick="dialogEdit()"
               data-options="iconCls:'icon-edit',plain:true">Edit</a>

            <a href="#" class="easyui-linkbutton" onclick="refreshDatagrid()" data-options="iconCls:'icon-reload', plain:true"></a>
            <!--<a href="#" class="easyui-linkbutton" onclick="showFilters()" data-options="iconCls:'icon-filter',plain:true">Filtrar</a>
            <div id="filter-container" style="display: none">
                <select  name="" class="easyui-combobox"  id="cc-filters" style="width:200px">
                    <option value="1">Fechas</option>
                    <option value="2">Status</option>
                    <option value="3">Tipo</option>
                </select>
            </div>
            <div id="dateFilter-container" style="display: none">
                <select id="cc-month" class="easyui-combobox" name="dept" style="width:200px;display: none">
                    <option value="1">Enero</option>
                    <option value="2">Febrero</option>
                    <option value="3">Marzo</option>
                    <option value="4">Abril</option>
                    <option value="5">Mayo</option>
                    <option value="6">Junio</option>
                    <option value="7">Julio</option>
                    <option value="8">Agosto</option>
                    <option value="9">Septiembre</option>
                    <option value="10">Octubre</option>
                    <option value="11">Noviembre</option>
                    <option value="12">Diciembre</option>
                </select>
                <select id="cc-year" class="easyui-combobox" name="dept" style="width:200px;display: none">
                    <option value="2019">2019</option>
                    <option value="2018">2018</option>
                    <option value="2017">2017</option>
                    <option value="2016">2016</option>
                    <option value="2015">2015</option>
                    <option value="2014">2014</option>

                </select>
            </div>
            <div id="typeFilter-container" style="display: none;width: 200px">
                <input type="text" class="easyui-combobox" url="../php/soporte/get_tipos.php"
                       data-options="valueField:'id', textField:'text'"
                       id="cc-tipo" style="width: 200px">

            </div>
            <a href="#" id="btn-filter" class="easyui-linkbutton" onclick="" style="display:none" data-options="iconCls:'icon-ok',plain:true">Ok</a>-->
        </div>

        <div id="dlg-edit" class="easyui-dialog" closed="true" data-options="modal:true, border:'thin', title:'Editar', buttons:'#bb'">

            <form method="post" id="form-solicitud" enctype="multipart/form-data">

                <ul>
                    <li>
                        <label for="titulo">titulo</label>
                        <input type="text" class="easyui-textbox" name="titulo" id="titulo" required/>
                    </li>
                    <li>
                        <label for="descripcion">Descripcion</label>
                        <textarea name="descripcion" id="descripcion" required></textarea>
                    </li>
                    <li>
                        <label for="estatus">Tipo</label>
                        <select class="easyui-combobox" name="estatus" id="estatus" required>

                            <option value="0">Cancelado</option>
                            <option value="1">Activo</option>
                            <option value="2">Terminado</option>

                        </select>
                    </li>

                    <li>

                        <label for="comentario">Comentario </label>
                        <textarea name="comentario" id="comentario" required></textarea>

                    </li>

                    <li>
                        <label for="fechaAprobada">Fecha aprobada</label>
                        <input type="text" class="easyui-datebox" id="fechaAprobada" name="fechaAprobada"
                               data-options="formatter:myformatter,parser:myparser">

                    </li>


                    <li>

                        <label for="file">Adjunto</label>
                        <input type="file" style="width: 100%;" id="file" multiple>

                    </li>

                </ul>

            </form>

        </div>

        <div id="detailModal" class="easyui-dialog" style="width: 50%;height: 70%;"
             data-options="resizable:false,modal:true,region:'center'">

        </div>

    </div>



    <script type="text/javascript">


        function refreshDatagrid() {
            $('#data-solicitud').datagrid('reload');
        }


        $(document).ready(function () {

            $('#cc-filters').combobox({
                onChange: function () {
                    let x = $('#cc-filters').val();
                    if (x === 1) {
                        $('#dateFilter-container').css('display', 'inline-block');
                        $('#typeFilter-container').css('display', 'none');
                    } else if (x === 2) {
                        $('#dateFilter-container').css('display', 'none');
                        $('#typeFilter-container').css('display', 'none');
                    } else {
                        $('#dateFilter-container').css('display', 'none');
                        $('#typeFilter-container').css('display', 'inline-block');
                    }
                }
            });


            $('#data-solicitud').datagrid({
                view: bufferview,

                url:'../../php/soporte/get_solicitudes.php',

                fitColumns:true,
                remoteSort:false,
                singleSelect:true,
                fit:true,

                onLoadSuccess: function (data) {

                    /*for(var i=0; i<data.rows.length; i++){

                        if (data.rows[i].archivos <= 0){

                            $(this).datagrid('getExpander', i).hide();
                        }
                    }*/
                },
                onDblClickRow: function (index, row) {
                    $('#detailModal').dialog({
                        closed: false,
                        draggable: false,
                        shadow: true,
                        border:'thin',
                        href: '../../php/soporte/get_files.php',
                        title: row.titulo,
                        queryParams: {
                            idSolicitud: row.idSolicitud,
                            descripcion: row.descripcion,
                            fechaPropuesta: row.fechaPropuesta,
                            nombre: row.nombre,
                            tipo: row.tipo,
                            comentario: row.comentario
                        },
                        onLoadSuccess: function (data) {
                        }

                    })

                }


            });

            $('#detailModal').dialog({
                closed: true
            })
        });



        function getAttach(value ,row) {

            return row.archivos > 0 ? '<img alt="attach" src="../../imagenes/icons/attach.gif" style="width: 20px;">': '';

        }

        function dialogEdit() {

            let row = $('#data-solicitud').datagrid('getSelections');

            if (row.length > 0) {

                if (row.length === 1) {
                    $('#dlg-edit').dialog('setTitle', 'Editar solicitud').dialog({
                        width: 500,
                        height: 500,

                        buttons: [{
                            text: 'Aceptar',
                            handler: function () {
                                updateS(row[0].idSolicitud);
                            }
                        }, {
                            text: 'Cancelar',
                            handler: function () {
                                $('#dlg-edit').dialog('close');
                            }
                        }]
                    }).dialog('open').dialog('center');
                    $('#form-solicitud').form('load', row[0]);
                    //$('#estatus').combobox('select', row[0].estatus);
                } else {
                    $.messager.alert('Aviso', 'Solo se puede un editar un objeto por vez');
                }

            } else {
                $.messager.alert('Aviso', 'Solicitud no seleccionada');
            }

        }


        function updateS(idSolicitud) {

            $('#dlg-edit').dialog('close');


            let fd = new FormData($('#form-solicitud')[0]);

            let ins = document.getElementById('file').files.length;


            for (let i=0; i < ins ; i++){

                fd.append('file[]', document.getElementById('file').files[i]);
            }

            fd.append('idSolicitud', idSolicitud);


            $.ajax({
                url:'../../php/soporte/update_solicitud.php',
                data: fd,
                type:'POST',
                contentType:false,
                beforeSend: function(){
                    $("<img src='../../imagenes/loaders/gears-3.gif' " +
                        "id='loading-estatus' alt='loader' class='loadin-image' />").appendTo("#content-solicitudes");

                },
                processData:false,
                success: function (data) {

                    $('#loading-estatus').remove();
                    let json = JSON.parse(data);


                    if (json.status){


                        let sent = json.send? 'Correo enviado!!! ': 'Correo No enviado';

                        $.messager.alert('Aviso', 'Actualizado Satisfactoriamente' + sent);

                        $('#data-solicitud').datagrid('reload');


                        $('#file').val('');

                    }else {

                        $('#dlg-edit').dialog('close');

                        $.messager.alert('Error', 'Error al Actualizar');

                    }


                },error:function(){

                    $('#loading-estatus').remove();
                    $('#file').val('');

                }

            });


           /* $('#form-solicitud').form('submit', {
                url: '../../php/soporte/update_solicitud.php?idSolicitud=' + idSolicitud,
                onSubmit: function () {
                    return $(this).form('validate');
                },
                data: fd,

                success: function (data) {


                    let json = JSON.parse(data);


                    if (json.status){

                        $('#dlg-edit').dialog('close');

                        $.messager.alert('Aviso', 'Actualizado Satisfactoriamente');

                        $('#data-solicitud').datagrid('reload');

                    }else {

                        $('#dlg-edit').dialog('close');

                        $.messager.alert('Error', 'Error al Actualizar');

                    }



                }

            });*/


        }

        /**
         *
         * @param index
         * @param {{estatus:int, fechaAprobada}} row
         */

        function myStyle(index, row) {

            if (row.estatus === 0) {
                return 'background-color: #ffe5eb;';
            } else if (row.estatus === 2) {
                return 'background-color: #e5ffe9;';
            } else if (row.estatus === 1 && row.fechaAprobada != null) {
                return 'background-color: #fff7e5;';
            }

        }

        function actionFormatter(val, row) {

            if (row.estatus === 0) {

                return `<svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 0 24 24" width="24px" fill="#e32510">
                        <path d="M0 0h24v24H0z" fill="none"/>
                        <path d="M12 2C6.47 2 2 6.47 2 12s4.47 10 10 10 10-4.47 10-10S17.53 2 12 2zm5 13.59L15.59 17 12 13.41 8.41 17 7 15.59 10.59 12 7 8.41 8.41 7 12 10.59 15.59 7 17 8.41 13.41 12 17 15.59z"/></svg>`;
                //return `<span style="background-color: #ff6666;padding:10px;color:#FFFFFF;border-radius:50%;font-weight:bold;text-shadow:0 0 3px #878787;">cancelado</span>`;

            } else if (row.estatus === 2) {

                //return `<span style="background-color: #83ff82;padding:10px;color:#FFFFFF;border-radius:50%;font-weight:bold;text-shadow:0 0 3px #878787;">hecho</span>`;

                return `<span><svg xmlns="http://www.w3.org/2000/svg" enable-background="new 0 0 24 24" height="24px" viewBox="0 0 24 24" width="24px" fill="#00c716">
                                <rect fill="none" height="24" width="24"/>
                                <path d="M22,5.18L10.59,16.6l-4.24-4.24l1.41-1.41l2.83,2.83l10-10L22,5.18z M19.79,10.22C19.92,10.79,20,11.39,20,12 c0,4.42-3.58,8-8,8s-8-3.58-8-8c0-4.42,3.58-8,8-8c1.58,0,3.04,0.46,4.28,1.25l1.44-1.44C16.1,2.67,14.13,2,12,2C6.48,2,2,6.48,2,12 c0,5.52,4.48,10,10,10s10-4.48,10-10c0-1.19-0.22-2.33-0.6-3.39L19.79,10.22z"/></svg></span>`;


            } else if (row.estatus === 1 && row.fechaAprobada != null) {

                return `<span><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 0 24 24" width="24px" fill="#e3e310"><path d="M0 0h24v24H0z" fill="none"/>
                        <path d="M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z"/>
                        <path d="M12.5 7H11v6l5.25 3.15.75-1.23-4.5-2.67z"/></svg></span>`;
                //return `<span style="background-color: #ffff56;padding:10px;color:#FFFFFF;border-radius:50%;font-weight:bold;text-shadow:0 0 3px #878787;box-sizing: border-box;">Espera</span>`;
            }else{

                let p = getPercent(row.t_start, row.t_end, row.t_now);
                let color = '';
                /*if(p <= 50 ){
                    color = '#79ff7f';
                }else if(p >50 && p <= 80){

                    color = '#fff86d';

                }else if(p > 80 && p<= 95)

                    color = '#ffaa4b';

                else if(p > 95){

                    color = '#ff615f';

                }*/

                color = '#00a5ff';

                p += '%';

                return `<div style="display: block;border-radius: 10px;box-sizing: border-box;background-color: #eaeaea;
                            margin: 5px;" class="progress-content"><div id="progress" style="width: ${p};background-color:
                        ${color};display: block;padding: 2px;border-radius: 10px;max-width: 100%;min-height: 15px;
                                box-sizing: border-box;color: #FFFFFF;font-weight: bold;text-shadow: 0 0 3px #9cb8d7;
                                 text-align: center;transition: width 2s"></div></div>`;

            }

        }


        function getPercent(starttime, endtime, today) {

            let timeBetweenStartAndEnd = (endtime - starttime) ;
            let timeBetweenStartAndToday = (today- starttime) ;

            //console.log([timeBetweenStartAndEnd, timeBetweenStartAndToday]);

            let percent = Math.round((timeBetweenStartAndToday / timeBetweenStartAndEnd) * 100);


            return percent > 100? 100 : percent ;
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

        function showFilters() {

            if ($('#filter-container').css('display') === 'none') {
                $('#filter-container').css('display','inline-block');
                $('#cc-filters').val(1);
                $('#dateFilter-container').css('display','inline-block');
                $('#btn-filter').css('display','inline-block');
            } else {
                $('#filter-container').css('display','none');
                $('#typeFilter-container').css('display','none');
                $('#btn-filter').css('display','none');
            }

            function filtrar(){
                $('#data-solicitud').datagrid({


                })

            }

        }


        function actionFilter(){

            $('.content-filter').toggleClass('active');


        }


    </script>

    <style>


        .filter-card .content-filter{

            width: 100%;
            height: 0;
            display: block;
            overflow: hidden;
            transition: height .5s;


        }


         .content-filter.active{

            height: 200px;


        }

        #form-solicitud ul {

            list-style: none;
            padding: 15px;

        }

        #form-solicitud li {
            margin-bottom: 5px;
        }

        #form-solicitud input, #form-solicitud textarea {
            border: 1px solid #ccc;
        }

        #form-solicitud label {

            width: 30%;
            float: left;
            text-align: left;
            display: inline-block;
        }


        .flex-container {
            display: flex;
        }



        .element-label {
            width: 40%;
            display: flex;
            justify-content: end;

        }

        .element-container {
            width: 60%;
        }



        .separator {
            margin: 3%;
            border: 0;
            height: 1px;
            background-image: -webkit-linear-gradient(left, #f9f9f9, #dbdada, #f9f9f9);
            background-image: -moz-linear-gradient(left, #f9f9f9, #dbdada, #f9f9f9);
            background-image: -ms-linear-gradient(left, #f9f9f9, #dbdada, #f9f9f9);
            background-image: -o-linear-gradient(left, #f9f9f9, #dbdada, #f9f9f9);
            width: 90%;
        }

        #desc {
            border: none;
        }

        .modal-ul {
            list-style-type: none;
            text-align: center;
        }



        .modal-ul li p {
            display: inline-block;
            text-align: start;
            width: 40%;
            padding-left: 2%;
        }

        .modal-ul li h4 {
            display: inline-block;
            text-align: end;
            width: 40%;
        }


    </style>


    <?php


} else {
    echo "accesso denegado";
}