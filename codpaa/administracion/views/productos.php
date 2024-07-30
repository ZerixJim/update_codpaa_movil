<?php

/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 29/07/14
 * Time: 16:35
 */

ob_start();
session_start();


?>


<div id="productos" class="content-padding">

    <?php if ($_SESSION['permiso'] >= 1) { ?>


        <style>

            #filters{

                display: flex;
                justify-content: space-between;


                margin: 15px;

            }

            #filters #finder{

                display: flex;


            }

            #finder a{

                border-radius: 0 8px 8px 0;
                background-color: #0D98FB;
                padding: 2px 5px;
            }

            #finder input{

                width:200px;
                border: 1px solid #ccc;
                border-right: none;
                padding: 8px;

            }

        </style>


        <div id="filters">


            <div id="comboboxes">

                <input id="marca-filter-producto" class="easyui-combobox" prompt="Marca" style="min-width: 250px;"/>

            </div>


            <div id="finder">

                <input id="search_prod" type="text" onKeyPress="enterPress(event);"
                       placeholder="Filtro..">
                <a href="#" class="easyui-linkbutton" style="color: white;font-weight: bold;" onClick="searchProd();">Buscar</a>


            </div>


        </div>



        <table id="dg_prod" class="easyui-datagrid"  toolbar="#toolbarP"
               fitColumns="true" singleSelect="false" idField="idProducto" sortName="idProducto"
               style="width: 100%;height: 550px;">

            <thead>

            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="idProducto" width="30" sortable="true" sorter="myIntSort">idProducto</th>
                <th data-options="field:'nombre'" width="100">Nombre</th>
                <th data-options="field:'presentacion'" width="40">Presentación</th>
                <th field="Marca" width="40">Marca</th>
                <!-- <th field="tipo" width="40">Tipo</th>-->
                <!-- <th data-options="field:'ck', title:'Tester', sortable:true" width="100"></th>-->
                <th width="50" data-options="field:'codigoBarras', title:'CodigoBarras'"></th>



            </tr>

            </thead>


        </table>

        <!-- toolbar promotores-->
        <div id="toolbarP" style="padding: 10px;">
            <?php if ($_SESSION['permiso'] >= 3 && ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == 5 )) { ?>


                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                   onclick="newProd()">Nuevo Producto</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                   onclick="removeProd()">Baja Producto</a>


                <!-- dialogo Promotores-->

            <?php }


            if(  $_SESSION['permiso'] >= 2   && ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '5') ){

                ?>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                   onclick="editProd()">Editar Producto</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                   onclick="asignProduct()">Asignar producto</a>

                <?php

            }



            ?>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true"
               onclick="$('#dg_prod').datagrid('reload');">Refrescar tabla</a>


            <?php

            if ($_SESSION['id_perfil'] == '5' || $_SESSION['id_perfil'] == '1' ){

                ?>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="makeTester()">Tester</a>




            <?php } ?>

            <!--Button export-->

            <a href="javascript:void(0)" id="btn-export-excel" class="easyui-linkbutton" iconCls="icon-excel" plain="true"
               style="display: none;">Exportar</a>

        </div>

        <!--Dialogo para crear nuevo producto -->
        <div id="dlg_prod" title="Infomacion del Producto" class="easyui-dialog" data-options="modal:true, onOpen:openDialog, border:'thin' "
             style="padding:10px 20px" closed="true" buttons="#dlg-buttons">


            <form id="fm_prodnew" method="post" class="form-style" novalidate>


                <ul>
                    <li>
                        <label for="nombre_prod">Nombre </label>
                        <input title="producto" name="nombre_prod" id="nombre_prod" class="easyui-validatebox"
                               style="width:180px" data-option="required:true">

                    </li>
                    <li>

                        <label for="presentacion">Presentacion</label>
                        <input title="presentacion" name="presentacion" id="presentacion" class="easyui-validatebox"
                               data-option="required:true">

                    </li>
                    <li>

                        <label for="Marca">Marca </label>
                        <input title="marca" class="easyui-combobox" name="Marca" id="Marca"
                               data-options="valueField:'idMarca',textField:'nombre',url:'../php/getMarca.php',required:true">

                    </li>
                    <li>
                        <label for="codigo">Codigo de Barras </label>
                        <input title="codigo" name="codigo" id="codigo" class="easyui-validatebox" data-options="required:true">
                    </li>
                    <li>
                        <label for="tipo_prod">Tipo Producto </label>
                        <input title="tipo" name="tipo_prod" id="tipo_prod" class="easyui-validatebox">
                    </li>
                    <li>
                        <label for="modelo_prod">Modelo </label>
                        <input title="modelo" name="modelo_prod" id="modelo_prod" class="easyui-validatebox">
                    </li>

                </ul>


            </form>


        </div>

        <!--Dialogo para asignar productos-->
        <div id="dialog_asig" title="Asignar" class="easyui-dialog" closed="true">

            <div id="content">

                <div>
                    <label for="left">Disponibles</label>
                    <select name="left" class="multiselect" id="left" multiple="multiple"></select>
                </div>

                <div id="buttons">

                    <button type="button" id="right_All_1" class="btn btn-block" onclick="formatToright()"> Asignar > </button>

                    <!--<button type="button" id="left_Selected_1" class="btn btn-block remove-all" onclick="" disabled>remover todas</button>-->
                    <button type="button" id="left_All_1" class="btn btn-block" onclick="formatToleft()"> < remover </button>


                </div>


                <div>
                    <label for="right">Asignada</label>
                    <select name="right" class="multiselect" id="right" multiple="multiple"></select>
                </div>


            </div>

        </div>



        <div id="dlg-buttons">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
               onclick="saveProd()">Guardar</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="hideDialogPro()">Cancelar</a>

        </div>

        <div id="prod_view" style="padding:10px 20px;display:none;">



        </div>
        <div id="prod_edit" style="padding:10px 20px;display:none;">
        </div>
        <div id="prod_remove" style="padding:10px 20px;display:none;">
            <span><br><br>&nbsp;&nbsp;Estas seguro de eliminar el Producto?</span>
        </div>

        <style type="text/css">

            *:focus{
                outline: none;
            }


            #fm_editProd ul{

                list-style: none outside;
                margin: 0;
                padding: 0;
            }

            #fm_editProd li{
                padding: 12px;
                border-bottom: 1px solid #eee;
                position: relative;
            }

            #fm_editProd li:last-child {
                border:none;
            }

            #fm_editProd label{
                width: 100px;
                float: left;
                text-align: left;
                display: inline-block;
            }

            #fm_editProd input {
                border: 1px solid #eee;
                height: 20px;
                width: 220px;
                padding: 5px 8px;

            }

            #fm_editProd input:focus{
                border: 1px solid #0b93d5;
                background-color: #9dd3ff;
            }

            #fm_editProd input, #fm_editProd textarea {

               /* box-shadow: 0 0 3px #ccc, 0 10px 15px #eee inset;*/
                border-radius: 2px;
                padding-right: 30px;
                -moz-transition: padding .25s;
                -webkit-transition: padding .25s;
                -o-transition: padding .25s;
                transition: padding .25s;
            }

            #dialog_asig #content{

                /*border: 1px solid #ccc;*/
                display: flex;
                justify-content: space-around;
                align-items: stretch;
                height: 90%;
                padding: 15px;

            }

            #content div{
                width: 30%;
                height: 100%;
            }

            #content div input{
                border: 1px solid #ccc;
                padding: 5px;
                margin-bottom: 2px;
            }

            #content div select{
                width: 100%;
                height: 100%;
            }



            #content select option{
                padding: 5px;
                border-bottom: 1px solid #e1e1e1;
            }

            #content #buttons{

                /*border: 1px solid #ccc;*/

                display: flex;
                flex-wrap: wrap;
                justify-content: space-around;

                align-content: space-around;

            }

            #content #buttons button{

                width: 60%;
                border: none;
                background-color: deepskyblue;
                padding: 15px 5px;
                color: #FFFFFF;


            }


        </style>



        <script>


            $('#marca-filter-producto').combobox({

                valueField: 'idMarca',

                textField: 'nombre',

                url: '../php/getMarca.php',

                required: true,

                multiple: false,

                groupField: 'cliente',

                formatter: marcaFormatter,

                icons: [ {
                    iconCls: 'icon-remove',
                    handler: function (e) {
                        let c = $(e.data.target);
                        let opts = c.combobox('options');
                        $.map(c.combobox('getData'), function (row) {
                            c.combobox('unselect', row[opts.valueField])
                        });
                    }
                }],



                onSelect: function (record) {



                },
                onUnselect: function (record) {


                },
                onLoadSuccess: function () {

                },

                onChange: function (newValue, oldValue) {

                    console.log(newValue);

                    if (newValue !== undefined && $.isNumeric(newValue)){

                        searchProdByBrand(newValue, "");
                    }else {

                        if (newValue === "")
                            searchProdByBrand();

                    }

                },
                /*validType: 'inList["#MarcaFot"]',*/

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

                },
                filter: function(q, row){
                    const opts = $(this).combobox('options');


                    //fitro para la busqueda por grupo o textfield

                    if (row[opts.groupField] != null){

                        return row[opts.groupField].toLowerCase().indexOf(q.toLowerCase()) >= 0
                            || row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;

                    } else {

                        return row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;
                    }

                }


            });

            function rowStyler(index, row) {

                if (row.estatus == 0){
                    return 'background-color:#ff8282;font-weight:bold;';
                }else {
                    if (row.ck == 'Probador'){
                        return 'background-color:#ffcf7c;font-weight:bold;';
                    }
                }

            }


            var idProducto = 0;
            $('#dg_prod').datagrid({
                method: 'get',
                onDblClickRow: onDblClickRow,
                remoteSort:false,
                url: '../php/productos/get_prod.php',
                view: bufferview,
                multiSort:true,
                rowStyler: rowStyler,
                detailFormatter:function(index, row){


                    return '<div class="ddv" style="padding:22px; width: 100%;">' +
                        '<table class="ddv" style="width: 50%;"></table></div>';

                },
                onExpandRow:function(index, row){

                    let count = $(this).datagrid('getRows').length;
                    /*other option is expandRow*/
                    for (let i=0; i< count ; i++ ){

                        if(i !== index)
                            $(this).datagrid('collapseRow', i);

                    }


                    let ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                    ddv.datagrid({


                        url:'../php/productos/get_productos_asig.php',
                        queryParams:{idProducto:row.idProducto},
                        fitColumns:true,
                        rownumbers:false,
                        height:'auto',
                        columns:[[
                            {field:'formato', title:'Asignado', width:300},
                            {field:'tipo', title:'Tipo', width:300}
                        ]],
                        onResize:function(){

                        },
                        loadMsg:'Cargando..',


                        onLoadSuccess:function(){
                            setTimeout(function(){
                                $('#dg_prod').datagrid('fixDetailRowHeight',index);
                            },0);
                        }

                    });

                    $(this).datagrid('fixDetailRowHeight', index);
                },
                onLoadSuccess:function(data){

                    //console.log(data);


                    for(let i=0; i<data.rows.length; i++){



                        if (data.rows[i].asign === 0){



                            $(this).datagrid('getExpander', i).hide();
                        }
                    }

                    $(this).datagrid('clearSelections');


                    showExport($(this));

                }

            });


            function showExport(data) {

                let selects = data.datagrid('getData');

                if(selects.total > 0){


                    $('#btn-export-excel').show();


                }else {
                    $('#btn-export-excel').hide();
                }

            }



            $('#btn-export-excel').click(function () {

                let datos = $('#dg_prod').datagrid('getRows');

                exportarExcel(datos);

            });



            function hideDialogPro() {
                $('#dlg_prod').dialog('close');
                $('#prod_view').hide();
                $('#prod_edit').hide();
                $('#prod_remove').hide();
            }
            function onDblClickRow(index) {

                let idProducto = $('#dg_prod').datagrid('getRows')[index]['idProducto'];
                $('#prod_view').show();

                $('#prod_view').dialog({
                    title: 'Datos Producto',
                    href: '../php/productos/view_prod.php?idProducto=' + idProducto,
                    width: 700,
                    height: 600,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#prod_view').dialog('close');
                        }
                    }],
                    modal:true
                });

            }
            function editProd() {
                const row = $('#dg_prod').datagrid('getSelections');


                if (row != null){


                    if (row.length == 1){
                        let idProd = row[0].idProducto;

                        $('#prod_edit').show();

                        $('#prod_edit').dialog({
                            title: 'Editar Producto',
                            href: '../php/productos/edit_prod.php?idProducto=' + idProd,
                            width: '50%',
                            height: '70%',
                            buttons: [{
                                text: 'Ok',
                                iconCls: 'icon-ok',
                                handler: function () {
                                    editProduc();
                                }
                            }, {
                                text: 'Cancel',
                                handler: function () {
                                    $('#prod_edit').dialog('close');
                                }
                            }]
                        }).dialog('center');
                    }else {
                        alert('por el momento solo se puede editar un producto a la vez');
                        $('#dg_prod').datagrid('clearSelections');
                    }


                }else {
                    alert('producto no selecionado');

                }

            }

            /*funcion para asignar productos*/
            function formatToright(){


                let formatos = $('#left').val();

                if (formatos != null){

                    $.ajax({
                        url:'../php/productos/asign_to_format.php',
                        method:'POST',
                        dataType:'json',
                        data:{idProducto:idProducto, formatos: formatos},
                        success:function(data){

                            if(data.success){

                                var size = data.inserted.length;

                                for(var i= 0; i < size; i++){


                                    $('#left').find('option[value='+data.inserted[i].idFormato+']').remove();

                                }


                                loadAsignFormats(data.idProducto);
                            }


                        }
                    });


                }


            }

            /*funcion para remover formatos a los productos*/
            function formatToleft(){

                let formatos = $('#right').val();

                /*console.log(formatos);*/

                if (formatos != null){

                    $.ajax({

                        url:'../php/productos/unasign_to_format.php',
                        method:'post',
                        dataType:'json',
                        data:{idProducto: idProducto, formatos: formatos},
                        success:function(data){
                            if(data.success){

                                const size = data.inserted.length;

                                for(let i= 0; i < size; i++){


                                    $('#right').find('option[value='+data.inserted[i].idFormato+']').remove();

                                }


                                loadAvailableFormats(data.idProducto);
                            }


                        }


                    });
                }




            }


            /*funcion para cargar los */
            function loadAvailableFormats(idProducto){

                $.ajax({
                    method:'post',
                    url:'../php/productos/get_formatos.php',
                    dataType:'json',
                    data:{idProducto:idProducto, solicitud:'disponibles'},

                    success:function(data){

                        $('#left').empty();
                        let count = 0;
                        $.each(data, function(i, item){
                            count++;
                            let option=  $('<option>',{
                                value:item.idFormato,
                                text:item.grupo +' '+item.cadena
                            }).css('background','url(../images/formatos/'+item.idFormato+'.png) no-repeat left')
                            .css('background-size', '32px auto')
                                .css('padding', '15px 5px 10px 40px');

                            $('#left').append(option).fadeIn('slow');


                        } );


                    }
                });

            }


            function loadAsignFormats(idProducto){

                $.ajax({
                    method:'post',
                    url:'../php/productos/get_formatos.php',
                    dataType:'json',
                    data:{idProducto:idProducto, solicitud:'asignados'},
                    success:function(data){

                        $('#right').empty();
                        let count = 0;
                        $.each(data, function(i, item){
                            count++;
                            let option=  $('<option>',{
                                value:item.idFormato,
                                text:item.grupo +' '+item.cadena
                            }).css('background','url(../images/formatos/'+item.idFormato+'.png) no-repeat left')
                                .css('background-size', '32px auto')
                                .css('padding', '15px 5px 10px 40px');

                            $('#right').append(option);
                        } );

                        //console.log(count);
                    }
                });

            }

            function asignProduct(){


                let selections = $('#dg_prod').datagrid('getSelections');

                console.log(selections[0]);

                if (selections.length > 0  && selections.length == 1){

                    idProducto = selections[0].idProducto;

                    $('#dialog_asig').dialog({
                        width:'70%',
                        height:550
                    }).dialog('open').dialog('center');
                    loadAvailableFormats(selections[0].idProducto);
                    loadAsignFormats(selections[0].idProducto);

                }else {
                    $.messager.alert('Aviso', 'Debes selecionar un solo producto');
                }


            }

            $('#left').multiselect({
                search: {
                    left: '<input type="text" name="q" placeholder="Formato.." />',
                }

            });

            $('#right').multiselect({
                search: {
                    left: '<input type="text" name="s" placeholder="Formato.." />'
                }

            });
            
            function makeTester() {

                let dataGrid = $('#dg_prod').datagrid();

                let productsSelect = dataGrid.datagrid('getSelections');

                if (productsSelect.length > 0){
                    console.log(productsSelect);

                    $.ajax({
                        type:'POST',
                        url:'../php/productos/set_tester.php',
                        data:{productos:productsSelect},
                        success:function(data){

                            console.log(data);
                            dataGrid.datagrid('reload');



                        },
                        error:function(jqXHR, textStatus, error){
                            console.log(error);
                        },
                        complete:function(jqXHR, textStatus){
                            dataGrid.datagrid('clearSelections');
                        }
                    });


                }else {
                    alert('selecciona por lo menos un producto');
                }

                
            }


            function removeProd() {
                let row = $('#dg_prod').datagrid('getSelections');

                if (row != null){
                    if (row.length == 1){
                        let idProd = row[0].idProducto;
                        document.getElementById('prod_remove').style.display = '';
                        $('#prod_remove').dialog({
                            title: 'Eliminar Producto',
                            width: 400,
                            height: 180,
                            buttons: [{
                                text: 'Ok',
                                iconCls: 'icon-ok',
                                handler: function () {
                                    removeProduc(idProd);
                                }
                            }, {
                                text: 'Cancel',
                                handler: function () {
                                    $('#prod_remove').dialog('close');
                                }
                            }]
                        });

                        $('#dg_prod').datagrid('clearSelections');

                    }else if(row.length == 0){

                        alert('Selecciona un producto a eliminar');

                    }else {
                        alert('Solo se puede eliminar un producto a la vez ');
                        $('#dg_prod').datagrid('clearSelections');
                    }



                }else {
                    alert('debes seleccionar un producto');
                }



            }

            function searchProdByBrand(idMarca, producto) {


                let dgProducto = $('#dg_prod');


                if (idMarca !== "") {


                    dgProducto.datagrid({

                        url: '../php/productos/get_prod.php',
                        queryParams: {idMarca: idMarca, producto : producto},

                    });


                } else {
                    dgProducto.datagrid('reload');
                }

            }


            function searchProd() {
                const buscarp = $('#search_prod').val();

                let marca = $('#marca-filter-producto').val();

                let dgProducto = $('#dg_prod');


                dgProducto.datagrid({

                    url: '../php/productos/get_prod.php',
                    queryParams: {idMarca: marca, producto : buscarp},

                });


            }



            function enterPress(e) {
                let key = e.keyCode || e.which;
                if (key === 13) {
                    searchProd();
                }

            }


            function openDialog() {

                let val = $('#marca-filter-producto').val();

                if( val !== undefined && $.isNumeric(val)){


                    setTimeout(() =>{

                        $('#Marca').combobox('select', val);

                    }, 100);


                }



            }


        </script>








    <? }  ?>

    <!-- FIN PROMOTORES-->


</div>

