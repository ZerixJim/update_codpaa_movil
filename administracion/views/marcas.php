<?php
/*
 * Created by PhpStorm.
 * User: grim
 * Date: 03/04/2018
 * Time: 12:04 PM
 */


session_start();

?>


<div id="marcas" style="padding: 15px;height: 100%;">


    <table id="marcas-datagrid" style="width: 30%;height: 90%;" fitColumns="true"
           idField="idMarca" toolbar="#toolbar">
    </table>
    <div id="toolbar">
        <a href="#" id="btnAddMarca" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="dlgMarca();"></a>
        <a href="#" id="btnEditMarca" class="easyui-linkbutton" style="display: none" iconCls="icon-edit" plain="true"
           onclick="editMarca()"></a>
        <a href="#" id="btnRemoveMarca" class="easyui-linkbutton" style="display: none;" iconCls="icon-remove"
           plain="true" onclick="removeMarca()"></a>
        <span class="textbox easyui-fluid searchbox" style="width: 170px">

            <input id="search-param" class="easyui-searchbox" data-options="prompt:'Buscar',searcher:doSearch">

        </span>

        <a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="refresh();"></a>



    </div>

    <div id="mar_new" style="width: 500px; height: 500px;" class="easyui-dialog"
         data-options="closed:true,modal:true,border:'thin',buttons:'#dlg-btn-new-brand'" b>
        <form id="fm_marca" class="form-style" novalidate style="margin:0;padding: 20px 50px" method="post">

            <ul>
                <li>
                    <h3>Informacion de la Marca</h3>
                </li>
                <li>
                    <input name="nombre" label="Nombre Marca" class="easyui-textbox" style="width: 100%"
                           required="true">
                </li>
                <li>
                    <select name="tipo" id="tipo_marca" label="Tipo de Marca" class="easyui-combobox" required="true" style="width: 100%;">
                        <option value="1">Temporal</option>
                        <option value="2">Fijo</option>
                        <option value="3">No Se Trabaja</option>
                        <option value="4">Competencia</option>
                    </select>
                </li>
                <li>

                    <input name="cliente" id="nombrecliente" label="Cliente" class="easyui-combobox" required="true"
                            data-options="url:'../php/clientes/get_cli.php',textField:'razonsocial',valueField:'idCliente'" style="width: 100%;"/>
                </li>
            </ul>


        </form>

        <div id="dlg-btn-new-brand">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="createNewBrand()" plain="true">Crear</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
               onclick="$('#mar_new').dialog('close')" plain="true">Cancelar</a>

        </div>


    </div>

    <style>

    </style>

    <script type="text/javascript">


        function refresh() {

            $('#search-param').val('');

            loadData();



        }

        function rowStyler(index, row) {

            if (row.estatus === 0){


                return 'background-color:#ff9e9e';

            }

        }


        function loadData() {

            let param = $('#search-param').val().length > 0 ? $('#search-param').val() : '';

            let url = getApi() + 'marcas';
            
            //let api = getApi();

            console.log(url);


            $('#marcas-datagrid').datagrid({
                url: url,
                method: 'GET',
                onClickRow: btnsEnabled,
                queryParams:{param:param},
                columns:[[

                    {field:'idMarca', title:'ID'},
                    {field:'nombre', title:'nombre'},
                    {field:'fechaCreacion', title:'Alta'}


                ]],
                loader: function (param, success, error) {
                    var opts = $(this).datagrid('options');
                    if (!opts.url) return false;
                    $.ajax({
                        type: opts.method,
                        url: opts.url,
                        data: param,
                        beforeSend: function (jqXHR) {
                            jqXHR.setRequestHeader("Authorization", "asfkdljflkajslkdfjlka");
                            jqXHR.setRequestHeader("Content-Type", "application/json");
                        },
                        success: function (data) {
                            success(data);
                        },
                        error: function () {
                            error.apply(this, arguments);
                        },
                        view: bufferview

                    });


                },
                onLoadSuccess: function (data) {
                },
                rowStyler:rowStyler

            });
        }

        $(document).ready(function () {

            loadData();

        });

        function dlgMarca() {


            $('#mar_new').form('clear');
            $('#mar_new').dialog('open').dialog('center').dialog('setTitle', 'Nueva Marca');




        }


        function createNewBrand() {

            let url = getApi() + 'marcas';



            let form = $('#fm_marca');

            if (form.form('validate')) {


                $.ajax({


                    beforeSend:function (xhr) {

                        xhr.setRequestHeader('Authorization', '392018308120489584909*_%34535_+');


                        $("<img src='../imagenes/loaders/gears-2.gif' " +

                            " id='loading-imagen' class='loadin-image' />").appendTo("#marcas")

                    },
                    url: url,
                    method:'post',

                    data: $('#fm_marca').serialize(),
                    success: function (data) {

                        $.messager.alert('Aviso', data.mensaje, 'info');

                        loadData();

                        $('#marcas-datagrid').datagrid('clearSelections');


                        $("#loading-imagen").remove();

                        $("#mar_new").dialog('close');

                    },
                    error:function (param1, param2, param3) {


                        let data = JSON.parse(param1.responseText);

                        $.messager.alert('Aviso', data.mensaje , 'error');

                        $("#loading-imagen").remove();

                        $("#mar_new").dialog('close');

                    }

                });

            }








        }




        function editMarca() {
            let row = $('#marcas-datagrid').datagrid('getSelections');


            if (row) {
                if (row.length > 1) {
                    $.messager.alert("Proceso no valido", "Selecciona solo una marca", "warning");
                } else {

                    //console.log(row);

                    //$('#mar_new').dialog('open').dialog('center').dialog('setTitle', 'Editar Marca');
                    //$('#fm_marca').form('load', row[0]);


                }
            }
        }


        function btnsEnabled() {
            let row = $('#marcas-datagrid').datagrid('getSelected');
            if (row) {
                $('#btnEditMarca').css("display", "inline-block");
                $('#btnRemoveMarca').css("display", "inline-block");
            } else {
                $('#btnEditMarca').css("display", "none");
                $('#btnRemoveMarca').css("display", "none");
            }

        }



        function removeMarca() {
            let row = $('#marcas-datagrid').datagrid('getSelections');


            if (row.length === 0){
                $.messager.alert("Proceso no valido", "marca no seleccionada", "warning");

            } else if (row.length > 1) {
                $.messager.alert("Proceso no valido", "Selecciona solo una marca", "warning");
            } else {

                $.messager.confirm('Confirm', 'Estas seguro(a) que deseas eliminar la marca ' + row[0].nombre, function (r) {

                    if (r){

                        let idMarca = row[0].idMarca;
                        let url = getApi() + 'marcas/id/'+idMarca;


                        $.ajax({


                            beforeSend:function (xhr) {

                                xhr.setRequestHeader('Authorization', 'asfdsafsdffdfddf');




                                $("<img src='../imagenes/loaders/gears-2.gif' " +

                                    " id='loading-imagen' class='loadin-image' />").appendTo("#marcas")

                            },
                            url: url,
                            method:'delete',
                            success: function (data) {

                                $.messager.alert('Aviso', data.mensaje, 'info');

                                loadData();

                                $('#marcas-datagrid').datagrid('clearSelections');

                                btnsEnabled();

                                $("#loading-imagen").remove();

                            },
                            error:function (param1, param2, param3) {


                                let data = JSON.parse(param1.responseText);

                                $.messager.alert('Aviso', data.mensaje , 'error');

                                $("#loading-imagen").remove();



                            }

                        });

                    }


                });



            }

        }

        function doSearch(value) {
            event.preventDefault();

            loadData();


        }
    </script>
</div>
