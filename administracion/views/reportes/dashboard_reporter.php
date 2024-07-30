<?php

session_start();

?>


<style>

    .lds-ripple {
        display: inline-block;
        height: 30px;
        position: absolute;
        width: 30px;
        top: 35%;
        left: 35%;

    }
    .lds-ripple div {
        position: absolute;
        border: 4px solid #fff;
        opacity: 1;
        border-radius: 50%;
        animation: lds-ripple 1s cubic-bezier(0, 0.2, 0.8, 1) infinite;
    }
    .lds-ripple div:nth-child(2) {
        animation-delay: -0.5s;
    }
    @keyframes lds-ripple {
        0% {
            top: 36px;
            left: 36px;
            width: 0;
            height: 0;
            opacity: 1;
        }
        100% {
            top: 0px;
            left: 0px;
            width: 72px;
            height: 72px;
            opacity: 0;
        }
    }



</style>

<div id="dashboard-reporter" class="content-padding">

    <h2>Dashboard / Reporter</h2>


    <div style="display: flex; justify-content: space-evenly; margin-top: 50px;">

        <select class="easyui-datebox" prompt="fecha" id="fecha-reporter" style="width: 150px;"></select>
        <select prompt="cliente" id="cliente" style="width: 150px;"></select>

        <a class="easyui-linkbutton" id="generate-button" data-options="iconCls:'icon-search'" onclick="loadCharts()" >Generar</a>


    </div>





    <div style="display: flex; justify-content: space-around;margin-top: 50px;margin-bottom: 50px;">

        <div id="content-visits-percent" style="width: 250px; height: 250px; display: inline-block; position: relative;">
            <canvas id="canvas" width="250" height="250" style="width:100%;height:100%;"></canvas>

        </div>

        <div id="content-captura-percent" style="width: 250px; height: 250px; display: inline-block;position: relative;">
            <canvas id="canvas2" width="250" height="250" style="width:100%;height:100%;"></canvas>

        </div>

        <div style="width: 250px; height: 250px; display: inline-block;">
            <canvas id="canvas3" width="250" height="250" style="width:100%;height:100%;"></canvas>

        </div>


    </div>


    <div>
        <table class="easyui-dategrid" id="porcentaje-tienda" style="min-height: 600px; max-height: 750px;"></table>


    </div>




</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js "></script>
<script>



    async function loadCharts(){

        let ctx = document.getElementById('canvas').getContext('2d');


        let date = $('#fecha-reporter').datebox('getValue');
        let cliente = $('#cliente').combobox('getValue');


        if (date !== '' || cliente !== ''){


            date = date.split('-');

            let month = date[1];
            let year = date[0];

            $.ajax({

                url : getApi() + 'porcentajes/promedio-visitas-cliente',
                beforeSend: (hrx) => {

                    $('#generate-button').linkbutton('disable');

                    $("<div class='lds-ripple'><div></div><div></div></div>").appendTo("#content-visits-percent");
                    hrx.setRequestHeader("Authorization", "4830284032094890328904832849028");

                },

                method: 'get',
                data: {client: cliente, year: year, month: month},

                success : (data) => {
                    let myChart =  new Chart(ctx,{
                        type: 'doughnut',
                        data: {
                            datasets: [{
                                label: 'Porcentaje Visitas',
                                percent: data.array[0].promedio,
                                backgroundColor: ['#5283ff']
                            }]
                        },
                        plugins: [{
                            beforeInit: (chart) => {
                                const dataset = chart.data.datasets[0];
                                chart.data.labels = [dataset.label];
                                dataset.data = [dataset.percent, 100 - dataset.percent];
                            }
                        },
                            {
                                beforeDraw: (chart) => {
                                    let width = chart.chart.width,
                                        height = chart.chart.height,
                                        ctx = chart.chart.ctx;
                                    ctx.restore();
                                    let fontSize = (height / 150).toFixed(2);
                                    ctx.font = fontSize + "em sans-serif";
                                    ctx.fillStyle = "#9b9b9b";
                                    ctx.textBaseline = "middle";
                                    let text = chart.data.datasets[0].percent + "%",
                                        textX = Math.round((width - ctx.measureText(text).width) / 2),
                                        textY = height / 2;
                                    ctx.fillText(text, textX, textY);
                                    ctx.save();
                                }
                            }
                        ],
                        options: {
                            maintainAspectRatio: false,
                            cutoutPercentage: 85,
                            rotation: Math.PI / 2,
                            legend: {
                                display: false,
                            },
                            tooltips: {
                                filter: tooltipItem => tooltipItem.index == 0
                            }
                        }
                    });

                },

                complete: () => {
                    $('#generate-button').linkbutton('enable');

                    $('#content-visits-percent').find('.lds-ripple').remove();
                },

                error: (param1, param2 , param3) => {

                }

            });



            $('#porcentaje-tienda').datagrid({


                url: getApi() + 'porcentajes/visitas-tienda-cliente-photos',
                queryParams :  {client: cliente, year: year, month: month},
                method: 'get',
                columns: [[
                    {field:'id_tienda', title:'IDT'},
                    {field:'sucursal', title:'sucursal'},
                    {field:'percent', title:'% visitas', formatter: visitPercent}

                ]],
                loader: function (param, success, error) {
                    let opts = $(this).datagrid('options');
                    if (!opts.url) return false;
                    $.ajax({
                        type: opts.method,
                        url: opts.url,
                        data: param,
                        beforeSend: function (jqXHR) {
                            jqXHR.setRequestHeader("Authorization", "423094803298490230940950934");
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


                }


            });


            await createCapturaPercent(month, year, cliente);



        }else {
            alert('campo fecha vacio');
        }



    }



    function createCapturaPercent(month, year, cliente){


        let ctx = document.getElementById('canvas2').getContext('2d');


        $.ajax({

            url : getApi() + 'porcentajes/captura',
            beforeSend: (hrx) => {

                $("<div class='lds-ripple'><div></div><div></div></div>").appendTo("#content-captura-percent");
                hrx.setRequestHeader("Authorization", "4830284032094890328904832849028");

            },

            method: 'get',
            data: {client: cliente, year: year, month: month},

            success : (data) => {

                let inven = data.array[0].inventarios;
                let frentes = data.array[0].frentes;

                let percent = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: ["Inventarios", "Frentes"],
                        datasets: [
                            {

                                label: "Captura",
                                backgroundColor: ["#3e95cd", "#8e5ea2"],
                                data: [inven,frentes]
                            }
                        ]
                    },
                    options: {
                        legend: { display: false },
                        title: {
                            display: true,
                            text: '% Captura en tiendas'
                        },
                        scales: {
                            xAxes: [{
                                stacked: true
                            }],
                            yAxes: [{
                                ticks: {
                                    beginAtZero: true,
                                    steps: 10,
                                    stepValue: 5,
                                    max: 100
                                }
                            }]
                        }
                    }


                });

            },

            complete: () => {

                $('#content-captura-percent').find('.lds-ripple').remove();
            },

            error: (param1, param2 , param3) => {

            }

        });



    }



    $('#cliente').combobox({

        url: '../../php/get_cliente.php',
        textField : 'razonsocial',
        valueField: 'idCliente'

    });

    let tds = false;
    $("#fecha-reporter").datebox({
        onShowPanel: function () {
            //Trigger the click event to pop up the month layer
            let p = $(this).datebox('panel');
            $(this).trigger('click');
            if (!tds)
                // Delayed trigger to obtain the month object, because there is a time interval between the above event trigger and the object generation
                setTimeout(function() {
                    tds = p.find('div.calendar-menu-month-inner td');
                    tds.click(function(e) {
                        //Prohibit bubbling to execute events bound to month by easyui
                        e.stopPropagation();
                        //Get the year
                        let year = /\d{4}/.exec(p.html())[0] ,
                            //month
                            month = parseInt($(this).attr('abbr'), 10);
                        //Hide the date object //Set the value of the date
                        $('#fecha-reporter').datebox('hidePanel').datebox('setValue', year + '-' + month);
                    });
                }, 0);
        },
        //Configure the parser and return the selected date
        parser: function (s) {
            if (!s) return new Date();
            let arr = s.split('-');
            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
        },
        //Configure formatter, only return year and month
        formatter: function (d) {
            let currentMonth = (d.getMonth()+1);
            let currentMonthStr = currentMonth < 10 ? ('0' + currentMonth) : (currentMonth + '');
            return d.getFullYear() + '-' + currentMonthStr;
        }
    });



    function visitPercent(val, row) {

        let p = val;
        let color = '';
        if(p <= 50 ){

            color = '#ff615f';
        }else if(p >50 && p <= 80){
            color = '#ffaa4b';
        }else if(p > 80 && p<= 95)

            color = '#fff86d';

        else if(p > 95){

            color = '#79ff7f';

        }

        p += '%';

        return `<div style="display: block;border-radius: 5px;box-sizing: border-box;background-color: #eaeaea;
                            margin: 5px;" class="progress-content"><div id="progress" style="width: ${p};background-color:
                        ${color};display: block;padding: 5px;border-radius: 5px;max-width: 100%;
                                box-sizing: border-box;color: #FFFFFF;font-weight: bold;text-shadow: 0 0 3px #878787;
                                 text-align: center;transition: width 2s">${p}</div></div>`;

    }

</script>
