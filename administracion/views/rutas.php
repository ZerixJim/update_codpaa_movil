<?php

session_start();
if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])):


    ?>

    <div id="cagar-rutas" class="content-padding">



        <div id="tools-ruta" >


            <form id="form_rutas" name="form_rutas" method="post" action="../clasess/carga_rutas.php"
                  enctype="multipart/form-data"  >


                <div id="content-file" style="display: flex;justify-content: space-around;">

                    <div>
                        <label for="semana_ruta">Semana de carga</label>
                        <select title="semana" id="semana_ruta" class="easyui-combobox" name="semana_ruta" style="width:100px;"
                                data-options="panelHeight:'auto',value:<?= date('W') + 0; ?>">

                            <? if($_SESSION['id_perfil'] == 1): ?>
                                <option value="<?= date('W') - 2; ?>">Semana <?= date('W') - 2; ?></option>
                                <option value="<?= date('W') - 1; ?>">Semana <?= date('W') - 1; ?></option>

                            <? endif;?>
                            <option value="<?= date('W') + 0; ?>">Semana <?= date('W') + 0; ?></option>
                            <option value="<?= date('W') + 1; ?>">Semana <?= date('W') + 1; ?></option>
                        </select>
                    </div>



                    <div>
                        <input id="file_rutas" class="file" name="file_rutas" type="file" accept=".xlsx" required>

                        <input type="submit"
                               style="padding: 15px; background-color: deepskyblue; border: none;color: #fff;"
                               value="Cargar Archivo"/>
                    </div>



                    <a href="javascript:void(0)" id="btn-guardiar" class="easyui-linkbutton" iconCls="icon-save" plain="true"
                       onclick="saveRutas()">Guardar</a>
                </div>


            </form>

        </div>

        <h3>Cargar Rutas</h3>
        <div id="rutas_cargadas">

            <table id="dgRutas" class="easyui-datagrid" style="min-height: 550px;max-height: 600px;"
                   data-options="autoRowHeight:true,singleSelect:false, fit:true, remoteSort:false" fitColumns="true">
                <thead>

                <tr>
                    <th data-options="field:'ck',checkbox:true"></th>
                    <th field="A" width="10" sortable="true">idPromotor</th>

                    <th field="B" width="10" sortable="true">idTienda</th>

                    <th field="tienda" width="45">Tienda</th>

                    <th field="C" width="8"
                        data-options="align:'center',editor:{type:'checkbox',options:{on:'1',off:'0'}}">L
                    </th>

                    <th field="D" width="8"
                        data-options="align:'center',editor:{type:'checkbox',options:{on:'1',off:'0'}}">Ma
                    </th>

                    <th field="E" width="8"
                        data-options="align:'center',editor:{type:'checkbox',options:{on:'1',off:'0'}}">Mi
                    </th>

                    <th field="F" width="8"
                        data-options="align:'center',editor:{type:'checkbox',options:{on:'1',off:'0'}}">J
                    </th>

                    <th field="G" width="8"
                        data-options="align:'center',editor:{type:'checkbox',options:{on:'1',off:'0'}}">V
                    </th>

                    <th field="H" width="8"
                        data-options="align:'center',editor:{type:'checkbox',options:{on:'1',off:'0'}}">S
                    </th>

                    <th field="I" width="8"
                        data-options="align:'center',editor:{type:'checkbox',options:{on:'1',off:'0'}}">D
                    </th>

                    <th field="J" width="15" data-options="editor:'text'">Rol</th>

                    <th field="idTipo" id="tipo" width="25" data-options="editor:{type:'combobox',
                        options:{
                            textField:'value',
                            valueField:'idTipo',
                            data:[{idTipo:1, value: 'Promotoria'},{idTipo:2, value:'Impulsor'}],
                            editable:false,
                            displayField:'value',
                            panelHeight:'auto'

                        }, formatter: formatoId}">Tipo</th>


                </tr>

                </thead>
            </table>


        </div>



    </div>


    <script type="text/javascript">


        //**********************Termina funcion rutas **************************//

        function saveRutas() {



            let semana = $('#semana_ruta').combobox('getValue');
            //let datosSource1 = $('#dgRutas').datagrid('getChecked');
            let datosSource1 = $('#dgRutas').datagrid('getSelections');
            //let datosSelct = $('#dgRutas').datagrid('getSelections');

            let dataJson = JSON.stringify(datosSource1);


            if (datosSource1.length > 0) {

                $.ajax({
                    beforeSend: function () {

                        $('#btn-guardiar').linkbutton('disable');

                        $("<img src='../imagenes/loaders/gears-3.gif' " +
                            "id='loading-rutas' alt='loader' class='loadin-image' />").appendTo("#rutas_cargadas");
                    },

                    type: 'POST',

                    url: '../php/rutas/save_rutas.php',

                    dataType: 'json',

                    data: {dataRutas: dataJson, semana_ruta: semana},

                    complete: function () {

                        $('#btn-guardiar').linkbutton('enable');

                    },

                    success: function(data){

                        $("#loading-rutas").remove();




                        if (data.insert) {
                            $.messager.alert('Rutas', 'se cargaron '+data.inserted.length + ' de un total de '+
                                data.total + ', </br> - fallidas '+ data.fail.length + ' las cuales se muestran a continuacion.');


                            //console.log(data.fail);

                            $('#dgRutas').datagrid('loadData', {"total":0,"rows":data.fail});


                        } else {
                            $.messager.alert('Rutas', 'Ocurrio un Problema', 'error');
                        }


                    },

                    error: function (jqx, status, thrown) {

                        //console.log('ajax loading error...' + jqx.responseText );

                        $.messager.alert('Aviso', 'error' + jqx.responseText, 'error');

                        $("#loading-rutas").remove();



                    }

                });

            } else {
                $.messager.alert('Rutas', 'Elige al Menos una Ruta');

            }


        }

        //****************************Funcion para agregar rastreo al mapa ******************//




        function selectTipo(value){
            //console.log(value);


        }

        function formatoId(index, row) { row.idTipo }


        function setValor() {
            //$('#tipo').combobox('setValue', {id:1, value: 'Promotoria'});
        }

        $(function () {

            //setValor();

            /*formulario para cargar la ruta*/


            $('#form_rutas').form({

                onSubmit: function () {
                    $.messager.progress({
                        title: 'Aguarde un momento',
                        msg: 'Cargando datos...'
                    });
                },

                success: function (data) {

                    $.messager.progress('close');
                    $('#dgRutas').datagrid({
                        data: jQuery.parseJSON(data)
                    });


                }
            });
        });

        $.extend($.fn.datagrid.methods, {
            editCell: function (jq, param) {
                return jq.each(function () {

                    let col;

                    //let opts = $(this).datagrid('options');
                    let fields = $(this).datagrid('getColumnFields', true).concat($(this).datagrid('getColumnFields'));
                    for (let i = 0; i < fields.length; i++) {
                        col = $(this).datagrid('getColumnOption', fields[i]);
                        col.editor1 = col.editor;
                        if (fields[i] !== param.field) {
                            col.editor = null;
                        }
                    }
                    $(this).datagrid('beginEdit', param.index);
                    let ed = $(this).datagrid('getEditor', param);
                    if (ed) {
                        if ($(ed.target).hasClass('textbox-f')) {
                            $(ed.target).textbox('textbox').focus();
                        } else {
                            $(ed.target).focus();
                        }
                    }
                    for (let j = 0; j < fields.length; j++) {
                        col = $(this).datagrid('getColumnOption', fields[j]);
                        col.editor = col.editor1;
                    }
                });
            },
            enableCellEditing: function (jq) {
                return jq.each(function () {
                    let dg = $(this);
                    let opts = dg.datagrid('options');
                    opts.oldOnClickCell = opts.onClickCell;
                    opts.onClickCell = function (index, field) {
                        if (opts.editIndex !== undefined) {
                            if (dg.datagrid('validateRow', opts.editIndex)) {
                                dg.datagrid('endEdit', opts.editIndex);
                                opts.editIndex = undefined;
                            } else {
                                return;
                            }
                        }
                        dg.datagrid('selectRow', index).datagrid('editCell', {
                            index: index,
                            field: field
                        });
                        opts.editIndex = index;
                        opts.oldOnClickCell.call(this, index, field);
                    }
                });
            }
        });

        $(function () {
            $('#dgRutas').datagrid().datagrid('enableCellEditing');
        })
    </script>

    <style type="text/css">

        #content-file{
            width: 70%;
            display: flex;
            padding: 20px;
            margin: auto;
            justify-content: space-around;
        }



        .upload-btn-wrapper {
            position: relative;
            overflow: hidden;
            display: inline-block;
        }

        .btn {
            border: 2px solid gray;
            color: gray;
            background-color: white;
            padding: 8px 20px;
            border-radius: 8px;
            font-size: 20px;
            font-weight: bold;
        }

        .upload-btn-wrapper input[type=file] {
            font-size: 100px;
            position: absolute;
            left: 0;
            top: 0;
            opacity: 0;
        }

    </style>
<? else:
        echo 'no has iniciado sesion';
        header('refresh:2,../index.php');
    endif;

?>
