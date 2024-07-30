<?php



?>


<style>

    .list-horizontal li{

        display: inline-block;


    }

</style>


<div id="content-percent" style="width: 100%;">


    <div class="list-horizontal" style="width: 100%;">

        <form method="post" id="promo-percent-form">


            <ul>



                <li>
                    <select id="month-percent" class="easyui-combobox" prompt="Semana" style="width: 80px;" required>

                        <?php

                        $f = date('Y-m-d');


                        for ($i = 0; $i >= -4  ; $i--){

                            $date = date('Y-m-d', strtotime("$f $i week"));
                            $week0 = new DateTime($date);
                            $value0 = $week0->format('W');

                            if((int) $value0  == 1){

                                $year = date( 'Y',
                                    strtotime( $week0->format('Y-m-d') .' sunday this week' ));
                            }else{

                                $year = $week0->format('Y');

                            }

                            $value0.= '-' . $year;

                            echo "<option value='$value0'>$value0</option>";
                        }



                        ?>





                    </select>

                </li>

                <li>

                    <select id="tipo-promotor" class="easyui-combobox" prompt="Tipo" style="width: 120px;" required>

                        <option value="1">Autoservicio</option>
                        <option value="2">Mayoreo</option>

                    </select>

                </li>

                <li>
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="width:80px">Generar</a>
                </li>

                <li>

                    <a href="javascript:void(0)"  class="easyui-linkbutton" iconCls="icon-excel" onclick="exportPercent()" plain="true">Exportar</a>

                </li>

            </ul>

        </form>



    </div>






    <table id="dg-percent" style="width: 800px;height: 600px;margin: 0 auto;"></table>




</div>



<script>


    function exportPercent(){

        let data = $('#dg-percent').datagrid('getRows');

        if (data.length > 0){

            exportarExcel(data);


        }

    }


    function color(val, row){


        let color = "#ff6a59";

        if(val != null){

            if (val >= 80){

                color = '#90ff70';

            }else if(val >= 50 && val <= 79 ){
                color = '#ffec40';
            }else if(val <= 49){

                color = '#ff6a59';
            }else {
                color = "#ffffff";
            }

            return "<div style='background-color:" + color + ";padding: 5px;'>"+ val +"%</div>";

        }else{

            return "<div style='background-color:" + color + ";padding: 5px;'>0%</div>";;

        }



    }


    function percent(val, row){



        return val!=null ? val +'%': val;
    }


    function submitForm() {


        let month = $('#month-percent').combobox('getValue');

        month = month.split('-');


        let tipo = $('#tipo-promotor').combobox('getValue');


        loadPercent(month[1], month[0], tipo);

    }


    function loadPercent(anio, month, tipo){

        let url = "promotores/percent";

        $('#dg-percent').datagrid({
            url: getApi() + url,
            method: 'GET',

            queryParams:{anio:anio, month:month, semana: month, tipoPromotor: tipo},
            columns:[[

                {field:'id_promotor', title:'ID P'},
                {field:'nombre', title:'Nombre'},
                {field:'solicitadas', title:'Solicitadas'},
                {field:'visitas', title:'Visitas'},
                {field:'mes', title:'Mes'},
                {field:'porcentaje', title:'Visitas', formatter: color},
                {field:'total_fotos', title:'fotos'},
                {field:'inventarios', title:'Inv/Tienda'},
                {field:'precios', title:'$/Tienda'}


            ]],
            singleSelect:true,
            loader: function (param, success, error) {
                const opts = $(this).datagrid('options');
                if (!opts.url) return false;
                $.ajax({
                    type: opts.method,
                    url: opts.url,
                    data: param,
                    beforeSend: function (jqXHR) {
                        jqXHR.setRequestHeader("Authorization", "90584392080959348905_%&+#__*");
                        jqXHR.setRequestHeader("Content-Type", "application/json");
                    },
                    success: function (data) {
                        success(data);
                    },
                    error: function () {
                        error.apply(this, arguments);
                    }

                });


            },
            onLoadSuccess: function (data) {
            },
            view:bufferview

        });

    }











</script>






