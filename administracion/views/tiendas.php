<?php
session_start();
include_once('../php/seguridad.php');
/**
 * Created by Dreamweaver.
 * User: Gustavo
 * Date: 3/02/15
 * Time: 10:13
 */


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    ?>


    <style>

        #filters {

            display: flex;
            justify-content: space-between;


        }

        #mannual-filter {

            display: flex;


        }

        #mannual-filter a {

            background-color: #0D98FB;
            border-radius: 0 5px 5px 0;

        }

        #mannual-filter input {

            border-radius: 5px 0 0 5px;
            width: 200px;
            max-height: 22px;
            border: 1px solid #ccc;
            border-right: none;
            padding: 5px;

        }


        #mannual-filter a {

            max-height: 22px;
            padding: 5px;
            line-height: 10px;

        }


    </style>


    <div title="Tiendas" id="tiendas" closable="true" class="content-padding">


        <!--filters -->
        <div id="filters" style="margin: 15px;">

            <div id="comboboxes">
                <input id="marca-tienda" class="easyui-combobox" prompt="Marca"
                       style="margin-left: 30px;margin-right: 30px;width: 250px;">
                <input id="tienda-formato" class="easyui-combobox" prompt="Formato"
                       style="margin-left: 30px;margin-right: 30px;width: 250px;">
                <input id="tienda-tipo" class="easyui-combobox" prompt="Tipo tienda"
                       style="margin-left: 30px;margin-right: 30px;width: 250px;">


                <? if ($_SESSION['id_perfil'] != 6): ?>
                    <input id="tienda-estructura" class="easyui-combobox" prompt="Estructura"
                           style="margin-left: 30px;margin-right: 30px;width: 250px;">

                    <input id="promotor-estructura" class="easyui-combobox" prompt="Promotor"
                        data-options="url:'../php/promotores/get_prom_estructura.php', textField:'nombre',
                                     valueField:'idCelular'"
                    >
                    
                    <input id="tienda-canal" class="easyui-combobox" prompt="Canal"
                           style="margin-left: 30px;margin-right: 30px;width: 250px;">
                           
                   
                     <select id="selEstatus" class="easyui-combobox" prompt="Estatus" style="margin-left: 30px;margin-right: 30px;width: 150px;"> 
                        <option value="1"></option>
                        <option value="2">Activo</option>
                        <option value="3">Inactivo</option>
                    </select>

                <? endif; ?>

            </div>


            <div id="mannual-filter">

                <input id="search_tien" placeholder="Filtro"
                       type="text" onKeyPress="tiendaWEnter(event);">

                <a href="#" class="easyui-linkbutton" style="color: #FFFFFF;" onClick="searchTienda();">Buscar</a>

            </div>

        </div>


        <!-- end filters -->

        <table id="dgTiendas" class="easyui-datagrid" fit="true" sortName="ID TIENDA" toolbar="#toolbarTienda" title="Listado de tiendas"
               fitColumns="true" idField="idTienda" style="max-height: 600px;min-height: 500px;width: 100%;">

            <thead>

            <tr>

                <th data-options="field:'ck',checkbox:true"></th>

                <th field="ID TIENDA" width="8" sortable="true" sorter="myIntSort">idTienda</th>

                <th field="CARTERA" width="10" formatter="vacanteChecker">Promotores</th>
                <th field="ESTADO SERVICIO" width="10" formatter="status"></th>

                <th field="GRUPO" width="8">Grupo</th>

                <th field="CADENA" width="8">Cadena</th>

                <th field="NO. ECO." width="8">N.Econo</th>

                <th field="SUCURSAL" width="10">Sucursal</th>

                <th field="ESTADO" width="8" sortable="false">Estado</th>

                <th field="MUNICIPIO" width="8">Municipio</th>

                <th field="MARCAS" width="15">Marcas</th>
            </tr>

            </thead>

        </table>


        <div id="toolbarTienda" style="padding: 10px;">
            <? if ($_SESSION['permiso'] >= 3 && $_SESSION['id_perfil'] == 1) { ?>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                   onclick="openDialogNewTienda()">Nueva
                    Tienda</a>

                <a href="javascript:void(0)" id="btn-tiendas-remover" style="display: none" class="easyui-linkbutton"
                   iconCls="icon-remove" plain="true" onclick="">Remover
                    Tienda</a>

            <? }
            if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '5' || $_SESSION['id_perfil'] == '2') {
                ?>
                <a href="javascript:void(0)" id="btn-tiendas-editar" style="display: none" class="easyui-linkbutton"
                   iconCls="icon-edit" plain="true"
                   onclick="editTien()">Editar Tienda</a>

            <? } ?>
            <a href="#" class="easyui-linkbutton" iconCls="icon-map" plain="true" onClick="mapa_tiendas()">Mapa</a>


            <a href="javascript:void(0)" id="btn-export-excel-tiendas" class="easyui-linkbutton" iconCls="icon-excel"
               plain="true">Exportar</a>

            
            <div id="total" style="display: inline-block;"></div>
            <div id="vacantes" style="display: inline-block;"></div>
            <div id="activas" style="display: inline-block;"></div>
            <div id="inactivas" style="display: inline-block;"></div>

        </div>


        <div id="dlgImport" class="easyui-dialog" style="width:1000px;height:280px;padding:10px 20px" closed="true"
             buttons="#dlg-btnIm">

            <table id="dgTiendas2" title="Tiendas" class="easyui-datagrid" fitColumns="true" idField="idTienda">

                <thead>

                <tr>

                    <th field="idTienda" width="30">idTienda</th>

                    <th field="grupo" width="12">Grupo</th>

                    <th field="cadena" width="12">Cadena</th>

                    <th field="numeroEconomico" width="25">N. Economico</th>

                    <th field="cadena" width="20">Cadena</th>

                    <th field="sucursal" width="20">Sucursal</th>

                    <th field="municipio" width="20">Municipio</th>

                    <th field="nombre" width="20">Estado</th>
                </tr>

                </thead>

            </table>


            <form id="fmCsvTiendas" method="post" novalidate>


                <div class="fitem">

                    <label>Archivo CSV</label>

                    <input name="csv" class="easyui-validatebox" type="file" required>


                </div>


            </form>

        </div><!-- end dialog import -->

        <div class="easyui-dialog" data-options="modal:true,title:'Nueva tienda'" closed="true" id="dlg-new-tienda"
             buttons="#dlg-btn-new-tienda"
             style="width:45%;padding: 15px; background-color: #f5f5f5;">


            <form id="form-new-tienda" method="post" class="form-style">

                <ul>


                    <li>

                        <input name="sucursalN" id="sucursalN" class="easyui-textbox"
                               required prompt="Sucursal">

                        <input name="economicoN" id="economicoN" class="easyui-textbox"
                               required prompt="economico">
                    </li>

                    <li>

                        <input name="razon-social" id="razon-social" class="easyui-combobox" prompt="razon social"
                            data-options="url:'../php/get_razon.php', textField:'nombre', valueField:'id'" required>

                    </li>

                    <li>

                        <input name="direccionN" id="direccionN" class="easyui-textbox"
                                prompt="Direccion"
                               required>

                        <input name="cpN" id="cpN" class="easyui-textbox" required prompt="CP">
                    </li>

                    <li>

                        <input name="coloniaN" id="coloniaN" class="easyui-textbox" required prompt="Colonia">
                    </li>
                    <li>

                        <input id="idEstadoN" class="easyui-combobox" name="idEstadoN" data-options="required:true" prompt="Estado">

                        <input id="idMunicipioN" data-options="valueField:'id',textField:'municipio'"
                               class="easyui-combobox" prompt="municipio"

                               name="idMunicipioN" required>
                    </li>

                    <li>

                        <input name="telefonoN" id="telefonoN" class="easyui-textbox" prompt="telefono">
                    </li>

                    <li>

                        <input name="latitudN" id="latitudN" class="easyui-textbox" required prompt="latitud">

                        <input name="longitudN" id="longitudN" class="easyui-textbox" required prompt="longitud">
                    </li>


                    <li>

                        <input id="idTipoTieN" class="easyui-combobox" name="idTipoTieN"
                               data-options="valueField: 'idTipo',textField: 'nombre',url: '../php/getTiendaTipo.php'"
                               prompt="Tipo tienda"
                               required>

                        <input id="idFormatoN" class="easyui-combobox" name="idFormatoN"
                               data-options="valueField: 'idFormato',textField: 'cadena',url: '../php/get_formato.php'"
                               prompt="Formato"
                               required>

                    </li>
                    
                    <li>
                        <input id="canalTiendaN" class="easyui-combobox" name="canalTiendaN"
                               data-options="valueField: 'canal',textField: 'canal',url: '../php/getTiendaCanal.php'"
                               prompt="canal"
                               required>
                    </li>

                </ul>


            </form>
        </div>


        <div id="dlg_view2" style="padding:0px 5px; display:none;">
        </div>
        <div id="dlg_editTie" style="padding:10px 20px; display:none;">
        </div>
        <div id="dlg_mapTie" style="padding:0px 5px; display:none;">
        </div>
        <div id="dlg-btnIm">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="ImpotarCsvTiendas()">Guardar</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
               onclick="$('#dlgImport').dialog('close')">Cancelar</a>

        </div>


        <div id="dlg-btn-new-tienda">
            <span id="loader-container" style="height: 100%:width:30px"></span>

            <a href="javascript:void(0)" class="easyui-linkbutton" id="btn-new-tienda" iconCls="icon-add"
               onclick="searchDuplicates()"
               plain="true">Crear</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" id="btn-new-tienda-cancel" iconCls="icon-cancel"
               onclick="$('#dlg-new-tienda').dialog('close')" plain="true">Cancelar</a>

        </div>
        <div id="dlg-btn-duplicates">

            <a href="javascript:void(0)" class="easyui-linkbutton" id="btn-confirm-duplicate" iconCls="icon-add"
               onclick="createNewTienda()"
               plain="true">Crear</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" id="btn-cancel-duplicate" iconCls="icon-cancel"
               onclick="$('#dlg-duplicates').dialog('close')" plain="true">Cancelar</a>

        </div>


    </div>
    <div class="easyui-dialog" id="dlg-duplicates" buttons="#dlg-btn-duplicates" title="Advertencia"
         style="width: 50%;height: 700px;max-height:70%" data-options="modal:true" closed="true">

        <div style="height: 10%;width: 100%;">
            <h3 style="text-align: center;margin: 5%">Se han encontrado estas tiendas del mismo formato cerca de la
                nueva tienda</h3>
        </div>
        <div class="" style="width: 93%;height: 100%;border: none;padding: 2%" id="grid-container">
            <table class="easyui-datagrid" style="width: 80%;min-height: 300px" fit="true" fitColumns="true"
                   idField="idTienda" id="dg-duplicates">
                <thead>
                <tr>
                    <th field="idTienda">id Tienda</th>
                    <th field="cadena">Cadena</th>
                    <th field="sucursal">Sucursal</th>
                    <th field="direccion">Direccion</th>
                    <th field="colonia">Colonia</th>
                </tr>
                </thead>

            </table>

        </div>

    </div>
    <script language="javascript">


        $('#marca-tienda').combobox({

            valueField: 'idMarca',

            textField: 'nombre',
            multiple: true,

            url: '../php/getMarca.php',
            groupField: 'cliente',
            onSelect: function (rec) {

                //console.log(rec)
                //searchTienda()

            },
            onUnselect: function (rec) {
                //console.log(rec);

                $(this).combobox('clear');

                //let values = $(this).combobox('getValues');
                //console.log(values.length);

            },

            filter: function (q, row) {
                const opts = $(this).combobox('options');


                //fitro para la busqueda por grupo o textfield

                if (row[opts.groupField] != null) {

                    return row[opts.groupField].toLowerCase().indexOf(q.toLowerCase()) >= 0
                        || row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;

                } else {

                    return row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;
                }


            },

            icons: [{
                iconCls: 'icon-remove',
                handler: function (e) {
                    let c = $(e.data.target);
                    let opts = c.combobox('options');
                    $.map(c.combobox('getData'), function (row) {
                        c.combobox('unselect', row[opts.valueField])
                    });
                }
            }],


            onHidePanel: function () {
                //console.log('el panel se cerro');

                const newValue = $(this).combobox('getValues');

                let optionsValids = [];

                let len = newValue.length;

                if (len > 0) {

                    for (let v = 0; v < len; v++) {

                        if ($.isNumeric(newValue[v])) {

                            optionsValids.push(newValue[v]);

                        }
                    }

                    if (optionsValids.length > 0) {

                        $(this).combobox('setValues', optionsValids);


                    }

                }

            }

        });


        $('#tienda-formato').combobox({

            url: '../php/get_formato.php',
            valueField: 'idFormato',

            textField: 'cadena',

            multiple: true,
            onUnselect: function (rec) {

                $(this).combobox('clear');
            },

            icons: [{
                iconCls: 'icon-remove',
                handler: function (e) {
                    let c = $(e.data.target);
                    let opts = c.combobox('options');
                    $.map(c.combobox('getData'), function (row) {
                        c.combobox('unselect', row[opts.valueField])
                    });
                }
            }]

        });


        $('#tienda-estructura').combobox({

            url: '../php/getTiendaEstructura.php',
            valueField: 'id',
            textField: 'nombre',
            icons: [{
                iconCls: 'icon-remove',
                handler: function (e) {
                    let c = $(e.data.target);
                    let opts = c.combobox('options');
                    $.map(c.combobox('getData'), function (row) {
                        c.combobox('unselect', row[opts.valueField])
                    });
                }
            }],
            multiple: false

        });


        $('#tienda-tipo').combobox({
            url: '../php/getTiendaTipo.php',
            textField: 'nombre',
            valueField: 'idTipo',
            onUnselect: function (rec) {

                $(this).combobox('clear');
            },
            multiple: true,

            icons: [{
                iconCls: 'icon-remove',
                handler: function (e) {
                    let c = $(e.data.target);
                    let opts = c.combobox('options');
                    $.map(c.combobox('getData'), function (row) {
                        c.combobox('unselect', row[opts.valueField])
                    });
                }
            }],
            onHidePanel: function () {
                //console.log('el panel se cerro');

                const newValue = $(this).combobox('getValues');

                let optionsValids = [];

                let len = newValue.length;

                if (len > 0) {

                    for (let v = 0; v < len; v++) {

                        if ($.isNumeric(newValue[v])) {

                            optionsValids.push(newValue[v]);

                        }
                    }

                    if (optionsValids.length > 0) {

                        $(this).combobox('setValues', optionsValids);

                    }

                }

            }


        });
        
        //CANAL DE LA TIENDA
        $('#tienda-canal').combobox({
            url: '../php/getTiendaCanal.php',
            textField: 'canal',
            valueField: 'canal',
            onUnselect: function (rec) {

                $(this).combobox('clear');
            },

            icons: [{
                iconCls: 'icon-remove',
                handler: function (e) {
                    let c = $(e.data.target);
                    let opts = c.combobox('options');
                    $.map(c.combobox('getData'), function (row) {
                        c.combobox('unselect', row[opts.valueField])
                    });
                }
            }],
            multiple: false
        });


        function searchTienda() {
            let buscar = $('#search_tien').val();


            if (buscar === "") {
                buscar = '*';
            }


            let marcas = $('#marca-tienda').combobox('getValues');
            let formatos = $('#tienda-formato').combobox('getValues');
            let tipoTienda = $('#tienda-tipo').combobox('getValues');
            let tipoEstructura = $('#tienda-estructura').combobox('getValue');
            let idPromo = $('#promotor-estructura').length> 0 ? $('#promotor-estructura').combobox('getValue') : '';
            let canal = $('#tienda-canal').combobox('getValue');
           // let estatus= $('#selEstatus').combobox('getValue');
          //  let estatus = document.getElementById("selEstatus").value;
            let estatus = $('#selEstatus').val();
             
           console.log(estatus);


            $('#dgTiendas').datagrid('clearSelections');

            $('#dgTiendas').datagrid({

                view: bufferview,
                singleSelect: false,
                url: '../php/tiendas/queryTiendas.php',
                autoRowHeight: false,
                method: 'get',
                fit: true,
                queryParams: {
                    buscarTienda: buscar,
                    idMarca: marcas,
                    formatos: formatos,
                    tipotienda: tipoTienda,
                    estructura: tipoEstructura,
                    idPromotor: idPromo,
                    tiendaCanal: canal,
                    estatus: estatus
                },
                onDblClickRow: onDblClickRowTienda,
                onClickRow: btnsEnabled,
                loadFilter: function (data) {

                    return data.result;

                },

                onLoadSuccess: function (data) {

                    let rows = $('#dgTiendas').datagrid('getRows');
                    
                    //console.log(rows);
                    
                    //TIENDAS ACTIVAS
                    let activas = rows.filter(item => {
                        return item.ESTATUS == "ACTIVA";
                    });
                    
                    //TIENDAS INACTIVAS
                    let inactivas = rows.filter( item => {
                        return item.ESTATUS == "INACTIVA";
                    });
                    
                    //TIENDAS VACANTES
                    let vacantes = rows.filter(item => {
                        
                        return item.CARTERA == "VACANTE" && item.ESTATUS == "ACTIVA";

                    });
                    
                    //TIENDAS EN SISTEMA
                    let total = rows.length;
                    
                    


                    $('#total').html('<div style="font-weight: bold;color: #000000;"> Total: ' + total + ' / '+'</div>');
                    $('#vacantes').html('<div style="font-weight: bold;color: #F3A60C;"> Vacantes: ' + vacantes.length + ' / ' + '</div>');
                    $('#activas').html('<div style="font-weight: bold;color: #24BF11;"> Activas: ' + activas.length + ' / ' + '</div>');
                    $('#inactivas').html('<div style="font-weight: bold;color: #CE1414;"> Inactivas: ' + inactivas.length + '</div>');


                },
                pageSize: 50,
                remoteSort: false,
                rowStyler: styleDown


            });


        }


        $('#idEstadoN').combobox({

            valueField: 'id',

            textField: 'nombre',

            url: '../php/get_estado.php',

            required: true,

            onLoadSuccess: function (record) {


            },

            onSelect: function (record) {

                let url = '../php/get_municipios.php?idEstado=' + record.id;
                let comboMunicipio = $('#idMunicipioN');

                comboMunicipio.combobox('clear');
                comboMunicipio.combobox('reload', url);

            }

        });

        function createNewTienda() {


            $.ajax({

                url: '../php/tiendas/create_new_tienda.php',
                data: $("#form-new-tienda").serialize(),
                beforeSend: function () {
                    $("#btn-confirm-duplicate").linkbutton('disable');
                    $("#btn-cancel-duplicate").linkbutton('disable');
                },
                success: function (data) {

                    $('#dlg-new-tienda').dialog('close');
                    $('#dlg-duplicates').dialog('close');

                    $("#form-new-tienda").form('clear');
                    $("#btn-confirm-duplicate").linkbutton('enable');
                    $("#btn-cancel-duplicate").linkbutton('enable');
                    $.messager.alert('Tienda', 'Tienda creada', 'success');
                },

                error: function (jqXHR, textStatus, errorThrown) {
                    //$.messager.alert('Error al crearse' + errorThrown + ' ' +jqXHR);
                    alert('error al crear' + errorThrown + textStatus + jqXHR);
                    $("#btn-confirm-duplicate").linkbutton('enable');
                    $("#btn-cancel-duplicate").linkbutton('enable');
                }
            });
            $('#dgTiendas').datagrid('reload');
        }


        function searchDuplicates() {


            let validate = $('#form-new-tienda').form('validate');
            let x = $('#latitudN').val();
            let y = $('#longitudN').val();
            let formato = $('#idFormatoN').combobox('getValue');

            if (validate) {
                $.ajax({
                    url: '../php/tiendas/search_duplicates_tiendas.php',
                    method: 'GET',
                    data: {
                        latitud: x,
                        longitud: y,
                        formato: formato
                    },
                    success: function (data) {
                        data = JSON.parse(data);

                        if (data.length > 0) {
                            $('#dlg-duplicates').dialog('open');
                            fillDuplicates(x, y, formato)

                        } else {
                            createNewTienda();
                        }
                    }

                })

            }
        }

        function fillDuplicates(x, y, formato) {
            $('#dg-duplicates').datagrid({
                singleSelect: true,
                url: '../php/tiendas/search_duplicates_tiendas.php',
                autoRowHeight: false,
                method: 'get',
                fit: true,
                queryParams: {
                    latitud: x,
                    longitud: y,
                    formato: formato
                }, beforeSend: function () {
                    $("<img src='../imagenes/loaders/gears-2.gif'  " +
                        " id='loading-ppt' />").appendTo("#loader-container");
                },
                onLoadSuccess: function () {
                    $("#loading-ppt").remove();
                }, error: function () {
                    $("#loading-ppt").remove();
                },
                rowStyler: styleDown
            });
        }


        function openDialogNewTienda() {
            $('#dlg-new-tienda').dialog('open');
        }


        function styleDown(index, row) {
            if (row.ESTATUS == "INACTIVA") {
                return 'background-color:#ff9393';
            }
        }


        function hideDialogTi() {
            $('#dlg_view2').hide();
            $('#dlg_editTie').hide();
            $('#dlg_mapTie').hide();
        }

        function onDblClickRowTienda(index) {


            let datagrid = $('#dgTiendas');

            let idTienda = datagrid.datagrid('getRows')[index]['ID TIENDA'];


            let dialogView = $('#dlg_view2');


            datagrid.datagrid('checkRow', index);

            dialogView.show();
            dialogView.dialog({
                title: 'Datos Tienda',
                href: '../php/tiendas/view_tienda.php?idTienda=' + idTienda,
                width: 900,
                height: 650,
                border: 'thin',
                buttons: [{
                    text: 'Ok',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#dlg_view2').dialog('close');
                    }
                }],
                onBeforeClose: function () {
                    datagrid.datagrid('unselectRow', index);
                },
                modal: true
            });

        }


        //todo-gus improvements implemetation, create a better way to make query

        function mapa_tiendas() {
            let busq = $('#search_tien').val();

            $('#dlg_mapTie').show();
            $('#dlg_mapTie').window({
                title: 'Mapa Tiendas',
                href: '../php/tiendas/view_tiendas_map.php?Busq=' + busq,
                width: 950,
                height: 620,
                resizable: true,
                buttons: [{
                    text: 'Ok',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#dlg_mapTie').dialog('close');
                    }
                }]
            });


        }

        function cargaMapaTnd(tiendas_m) {

            let directionsDisplay1 = new google.maps.DirectionsRenderer();

            let mapOptions = {
                zoom: 5,

                center: new google.maps.LatLng(22.35006135229113, -101.55759218749995),

                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            let map_ruta = new google.maps.Map(document.getElementById('Mapa_tiendas'), mapOptions);

            directionsDisplay1.setMap(map_ruta);

            for (let i = 0; i < tiendas_m.length; i++) {

                let tien_m = tiendas_m[i];

                let sucur = tien_m.sucursal;

                //***********************
                let msj = '<div id="content">' +
                    '<h2 id="firstHeading" class="firstHeading">Sucursal ' + tien_m.idTienda + '</h2>' +
                    '<div id="bodyContent">' +
                    '<p>' + tien_m.grupo + '</p>' +
                    '<p>' + tien_m.direcc + '</p>' +
                    '</div>' +
                    '</div>';


                let myLatLngTiendas = new google.maps.LatLng(tien_m.x, tien_m.y);

                markerTiendas = new google.maps.Marker({

                    position: myLatLngTiendas,

                    map: map_ruta,

                    icon: '../imagenes/rastreo/tienda.png',

                    title: sucur


                });
                messageStore(markerTiendas, msj);


            }

        }

        function editTien() {
            let row = $('#dgTiendas').datagrid('getSelections');


            if (row.length == 1) {
                
                //console.log(row);
                let idTien = row[0]["ID TIENDA"];
                //console.log(idTien);

                $('#dlg_editTie').show();
                $('#dlg_editTie').dialog({
                    title: 'Editar Tienda',
                    href: '../php/tiendas/edit_tienda.php?idTienda=' + idTien,
                    width: 700,
                    height: 550,
                    modal: true,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            save_editTien(idTien);
                        }
                    }, {
                        text: 'Cancel',
                        handler: function () {
                            $('#dlg_editTie').dialog('close');
                            hideDialogTi();
                        }
                    }]
                });
            } else if (row.length == 0) {
                $.messager.alert('Warning', 'Tienda no seleccionada');
            } else {
                $.messager.alert('Warning', 'varias tiendas seleccionadas');
            }

        }

        $('#btn-export-excel-tiendas').click(function () {

            let stores = $('#dgTiendas').datagrid('getRows');

            if (stores.length > 0) {
                exportarExcel(stores);
            } else {
                $.messager.alert('warning', ' no se encontró contenido ');
            }


        });

        function btnsEnabled() {
            let row = $('#dgTiendas').datagrid('getSelected');
            if (row) {
                $('#btn-tiendas-remover').css("display", "inline-block");
                $('#btn-tiendas-editar').css("display", "inline-block");
            } else {
                $('#btn-tiendas-editar').css("display", "none");
                $('#btn-tiendas-remover').css("display", "none");
            }
        }


        function vacanteChecker(val, row){

            if (val === 'VACANTE'){

                return `<span style='padding: 2px;background-color: #B43D5E;border-radius: 8px;color: #e8e8e8;font-weight: bold;'>${val}</span>`;

            } else return val;

        }


        function status(val, row){
            //console.log(row);
            if (val !== null){

                if (val.includes('Sin')){
                    return `<span style="padding: 3px;background-color: #ef9730;border-radius: 10px; font-weight: bold; color: #F8F8F8; text-shadow: 0 0 1px #6D6709">${val}</span>`;
                }else{
                    return `<span style="padding: 3px;background-color: #FBF25E;border-radius: 10px; font-weight: bold; color: #F8F8F8; text-shadow: 0 0 1px #6D6709">${val}</span>`;
                }

            } else if(row["MARCAS"] == null){
                return `<span style="padding: 3px;background-color: #FBF25E;border-radius: 10px; font-weight: bold; color: #F8F8F8; text-shadow: 0 0 1px #6D6709">Sin servicio</span>`;
            }return val;
        }


    </script>


    <?


} else {

    echo "Access Denied";

}


?>







