<? session_start(); ?>
<?
if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    include_once('../connexion/DataBase.php');
    include_once('../php/seguridad.php');
    $manager = DataBase::getInstance();
    ?>


    <div id="marcasPcenter" class="content-padding" data-options="region:'center',title:'Resultado',split:true,collapsible:false"
         style="width: auto;height: 100%;">


        <div class="row">

            <div class="col-xs-70">
                <input name="busca_promotor" type="text" id="busca_promotor"
                       placeholder="Nombre promotor, o ID" style="padding: 15px;width: 100%;">

            </div>


            <div class="col-xs-5">
                <select title="Marcas disponibles" name="from[]" id="marcas-disponibles-p" class="multiselect select" multiple="multiple"></select>

            </div>

            <div class="col-xs-2">

                <button type="button" id="right_All_1" class="btn btn-block action-buttons" onclick="toRight()">
                    <legend class="text-uppercase font-size-sm font-weight-bold">Asignar ></legend>
                </button>
                <!--<button type="button" id="right_Selected_1" class="btn btn-block">>></button>-->
                <button type="button" id="left_Selected_1" class="btn btn-block action-buttons" onclick="deleteAllMarcas()">
                    <legend class="text-uppercase font-size-sm font-weight-bold">remover todas</legend>
                </button>
                <button type="button" id="left_All_1" class="btn btn-block action-buttons" onclick="toLeft()">
                    <legend class="text-uppercase font-size-sm font-weight-bold">< remover</legend>
                </button>


            </div>

            <div class="col-xs-5">
                <select title="Marcas Asignadas" name="to[]" id="marcas-asign-p" size="8" class="select" multiple="multiple"></select>

            </div>


        </div>


    </div>




    <script type="application/javascript">


        let idPromotor = 0;

        $('#busca_promotor').autocomplete({

            serviceUrl: '../php/search_prom.php',
            minChars: 2,
            onSelect: function (sugestions) {


                $('#marcas-disponibles-p').empty();
                $('#marcas-asign-p').empty();
                if (sugestions.data.idCelular != undefined){

                    idPromotor = sugestions.data.idCelular;
                    showMarcas(sugestions.data.idCelular);
                    showMarcasAsign(sugestions.data.idCelular)

                }

                //console.log('on Select');

            },
            triggerSelectOnValidInput: false

        });


       /* $('#promotor-marca').tagbox({

            url:'../php/promotores/get_prom_lite.php',
            valueField:'idCelular',
            hasDownArrow: true,
            limitToList: true,
            textField:'nombre',
            prompt: 'promotor',
            tagStyler:function (value) {
                return 'color:#5697ff';
            },
            tagFormatter: function(value,row){
                var opts = $(this).tagbox('options');

                var nombre = row[opts.textField].split(' ')[0];


                return nombre + ' ID ' + row.idCelular ;
            }

        });*/





        async function showMarcas(idPromotor){
            $('.action-buttons').prop('disabled', true);
            $.ajax({

                type:'POST',
                dataType:'json',
                data:{idPromotor: idPromotor},
                url:'../php/promotores/lista_marcasP.php',
                success:function(data){


                    /*console.log(data);*/
                    $.each(data.marcasDisponibles, function (i, item) {


                        const groupF = $('#marcas-disponibles-p')
                            .find('optgroup[label="'+item.cliente+'"]');

                        const encontrado = groupF.length;


                        let option = $('<option>', {
                            value: item.idMarca,
                            text: item.nombre
                        });

                        if (encontrado <= 0){

                            let group = $('<optgroup>', {
                                label: item.cliente
                            });


                            group.append(option);
                            $('#marcas-disponibles-p').append(group);


                        }else {

                            groupF.append(option);

                        }



                    });

                    /*console.log(data);*/
                }


            });

            await $('.action-buttons').prop('disabled', false);
        }

        function toRight(){

            addMarca(idPromotor);
        }

        function toLeft(){
            deleteMarcas(idPromotor);
        }

        function addMarca(idPromotor){

            const selectMarcasDispo = $('#marcas-disponibles-p');


            if (selectMarcasDispo.val() != null){

                $.ajax({
                    type:'POST',
                    dataType:'json',
                    data:{selects: selectMarcasDispo.val(), idPromotor:idPromotor},
                    url:'../php/promotores/add_marca.php',
                    success:function(data){


                        let selecctions = selectMarcasDispo.val();

                        for (let i = 0; i < selecctions.length ; i++ ){
                            $('#marcas-disponibles-p').find('option[value='+selecctions[i]+']').remove();
                        }

                        $('#marcas-asign-p').empty();
                        showMarcasAsign(idPromotor);


                    }
                });

            }else {
                $.messager.alert('Aviso', 'Marca no seleccionada');

            }

        }

        function showMarcasAsign(idPromotor){

            $.ajax({

                type:'POST',
                dataType:'json',
                data:{idPromotor: idPromotor},
                url:'../php/promotores/marcas_asign_promotor.php',
                success:function(data){



                    $.each(data, function(i, item){

                        const groupF = $('#marcas-asign-p')
                            .find('optgroup[label="'+item.cliente+'"]');

                        let option=  $('<option>',{
                            value:item.idMarca,
                            text:item.nombre
                        });

                        if (groupF.length <= 0){


                            let group = $('<optgroup>', {
                                label: item.cliente
                            });


                            group.append(option);
                            $('#marcas-asign-p').append(group);

                        }

                        groupF.append(option);


                    } );

                }


            });

        }


        function deleteMarcas(idPromotor){

            const selectMarcasDispo = $('#marcas-asign-p');


            if (selectMarcasDispo.val() != null){

                $.ajax({
                    type:'POST',
                    dataType:'json',
                    data:{marcas: selectMarcasDispo.val(), idPromotor:idPromotor},
                    url:'../php/promotores/delete_marca.php',
                    success:function(data){

                        var selecctions = selectMarcasDispo.val();

                        for (var i = 0; i < selecctions.length ; i++ ){
                            $('#marcas-asign-p').find('option[value='+selecctions[i]+']').remove();
                        }

                        $('#marcas-disponibles-p').empty();
                        showMarcas(idPromotor);

                    }
                });

            }else {
                $.messager.alert('Aviso', 'Marca no seleccionada');

            }

        }

        function deleteAllMarcas(){

            const selectMarcasDispo = $('#marcas-asign-p');

            let marcas = [];

            if (selectMarcasDispo.find('option').length > 0){

                selectMarcasDispo.find('option').each(function(i, item){

                    marcas[i] = item.value;

                });


                $.ajax({
                    type:'POST',
                    dataType:'json',
                    data:{marcas: marcas, idPromotor:idPromotor},
                    url:'../php/promotores/delete_all_marcas.php',
                    success:function(data){

                        let selecctions = marcas;

                        for (let i = 0; i < selecctions.length ; i++ ){
                            $('#marcas-asign-p').find('option[value='+selecctions[i]+']').remove();
                        }

                        $('#marcas-disponibles-p').empty();
                        showMarcas(idPromotor);

                    }
                });


            }else {
                $.messager.alert('Error', 'No hay marcas disponibles');
            }



        }



        $('#form1').submit(function () {
            return false;
        });

        $('#marcas-disponibles-p').multiselect({
            search: {
                left: '<input type="text" name="q" class="form-control" placeholder="Marcas Disponibles" />'
            }

        });

        $('#marcas-asign-p').multiselect({
            search: {
                left: '<input type="text" name="s" class="form-control" placeholder="Marca Asignada" />'
            }

        });




    </script>

    <style>

        .autocomplete-suggestions {
            border: 1px solid #b8b8b8;
            background: #FFF;
            border-radius: 0 0 2px 2px;
            padding: 15px;

            color: #7a7a7a;

            overflow: auto;

        }

        .autocomplete-selected {
            background: #f0b589;

        }
        .autocomplete-suggestions strong {
            font-weight: bold;
            color: #3eaaea;

        }
        .autocomplete-group {
            padding: 2px 5px;
        }
        .autocomplete-group strong {
            display: block;
            border-bottom: 1px solid #000;
        }


    </style>




<? } else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>