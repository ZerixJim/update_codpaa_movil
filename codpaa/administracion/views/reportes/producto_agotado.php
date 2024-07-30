<?php

session_start();






if (isset($_SESSION["id_perfil"])){
?>


    <div class="content-padding">



        <input class="easyui-combobox" id="marca-agodato" prompt="Marca">

        <input class="easyui-combobox"  id="date-agotados"
               data-options="panelHeight:'auto',valueField:'label', textField:'text', url:'../php/get_semanaLab3.php'" prompt="Semana">

        <a class="easyui-linkbutton" id="create-grid-agotados" onclick="createGrid()">Buscar</a>
        <a class="easyui-linkbutton" iconcls="icon-excel" onclick="exportAgotados()"></a>
        <table id="dg-agotados" style="min-height: 500px;max-height: 600px;"></table>



    </div>


    <script>

        function createGrid(){

            let brand = $('#marca-agodato').combobox('getValue');
            let date = $('#date-agotados').combobox('getValue');
            if (brand.length > 0 && date.length > 0){
                loadData(brand, date);
            }else {
                alert("campos requeridos");
            }

        }

        function loadData(idMar, date){

            $("#dg-agotados").datagrid({

                url: getApi() + 'producto/agotado',
                columns : [[
                    {field: 'id_tienda', title:'IDT' },
                    {field: 'sucursal', title:'Sucursal' },
                    {field: 'marca', title:'Marca' },
                    {field: 'producto', title:'Producto' },
                    {field: 'presentacion', title:'Presentacion' },
                    {field: 'agotado', title:'Agotado' , align:'center', formatter:agotadoFormatter},
                    {field: 'pre-agotado', title:'Pre-Agotado', align:'center', formatter:agotadoFormatter },
                    {field: 'disponible', title:'Disponible', align:'center', formatter:agotadoFormatter },
                    {field: 'fecha', title:'fecha', align:'center' }
                ]],
                method: 'GET' ,
                queryParams: {idMarca:idMar, date: date} ,
                loader: (param, success, error) => {

                    const opts = $("#dg-agotados").datagrid('options');
                    if (!opts.url) return false;

                    $.ajax({
                        type: opts.method,
                        url: opts.url,
                        data: param,
                        beforeSend: jqXHR => {
                            jqXHR.setRequestHeader("Authorization", "90584392080959348905");
                            jqXHR.setRequestHeader("Content-Type", "application/json");

                            $('#create-grid-agotados').linkbutton('disable');

                        },
                        success: function (data){
                            success(data);
                        },
                        error: function (){
                            error.apply(this, arguments);
                        },
                        complete: () =>{

                            $('#create-grid-agotados').linkbutton('enable');
                        }
                    });

                },
                onLoadSuccess: ()=>{



                }
            });

        }

        function agotadoFormatter(val, row){

            if (val){

                return `<span><svg xmlns="http://www.w3.org/2000/svg" enable-background="new 0 0 24 24" height="24px" viewBox="0 0 24 24" width="24px" fill="#00c716">
                                <rect fill="none" height="24" width="24"/>
                                <path d="M22,5.18L10.59,16.6l-4.24-4.24l1.41-1.41l2.83,2.83l10-10L22,5.18z M19.79,10.22C19.92,10.79,20,11.39,20,12 c0,4.42-3.58,8-8,8s-8-3.58-8-8c0-4.42,3.58-8,8-8c1.58,0,3.04,0.46,4.28,1.25l1.44-1.44C16.1,2.67,14.13,2,12,2C6.48,2,2,6.48,2,12 c0,5.52,4.48,10,10,10s10-4.48,10-10c0-1.19-0.22-2.33-0.6-3.39L19.79,10.22z"/></svg></spa>`;

            }

        }


        $('#marca-agodato').combobox({

            valueField: 'idMarca',
            textField: 'nombre',
            url: '../../php/getMarca.php',
            required: true,
            groupField: 'cliente',
            onSelect: opt => {
                //loadData(opt.idMarca);

            }

        });

        function exportAgotados(){

            let value = $('#dg-agotados').datagrid('getRows');

            if (value.length > 0 ){
                exportarExcel(value);

            }else {

                alert("no hay datos par exportar");

            }

        }

    </script>





<?php
}else{

    echo "Unauthorized";
    http_response_code(401);

}



