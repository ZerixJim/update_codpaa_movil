<?php
session_start();
$idUsuario = $_SESSION['idUser'];



?>



<div id="reporte-censo-palette" style="padding-left: 50px; padding-right: 50px; padding-top: 50px; box-sizing: border-box;">



    <a class="easyui-linkbutton" onclick="getDta()" iconCls="icon-excel">export</a>

    <table id="dg-palette"></table>





</div>


<script>


    $('#dg-palette').datagrid({


        url: getApi() + 'tiendas/palette' ,
        method: 'get',

        columns:[[
            {field:'id_tienda', title:'ID_T'},
            {field:'id_promotor', title: 'ID_P'},
            {field:'grupo', title:'Razon Social'},
            {field:'sucursal', title:'Sucursal'},
            {field:'estado', title:'Estado'},
            {field:'nielsen', title:'Nielsen'},
            {field:'municipio', title:'Municipio'},
            {field:'categoria', title:'Categoria'},
            {field:'frentes', title:'Frentes', formatter:categoryFormatter},
            {field:'tonos', title:'Tonos', formatter:categoryFormatter},
            {field:'Nutrisse', title:'Nutrisse', formatter:priceFormatter},
            {field:'Koleston', title:'Koleston', formatter: priceFormatter},
            {field:'Palette', title:'Palette', formatter:priceFormatter}

        ]],


        loader: function(param, success, error){
            let opts = $(this).datagrid('options');
            if (!opts.url) return false;
            $.ajax({
                type: opts.method,
                url: opts.url,
                data: param,
                dataType: 'json',
                headers:{
                    'Authorization' : '542390850932905809438095809384-348@&'
                },
                success: function(data){
                    success(data);
                },
                error: function(){
                    error.apply(this, arguments);
                }
            });
        }



    });


    function priceFormatter(val, row) {

        if (row.categoria === 'Si'){

            if (val !== undefined){
                return val;
            } else {
                return '<span style="color:#1c1c1c;">NO</span>';
            }

        }else {
            return '<span style="color:#1c1c1c;">N/A</span>';
        }

    }

    function categoryFormatter(val, row) {


        if (row.categoria == 'Si'){

            return val;

        }else {
            return '<span style="color:#1c1c1c;">N/A</span>';
        }


    }


    function getDta() {


        $('#dg-palette').datagrid('toExcel', {
            filename: 'datagrid.xls',
            worksheet: 'Worksheet'
        });


    }


</script>


<script src="../../plugins/easyui/datagrid-export.js"></script>