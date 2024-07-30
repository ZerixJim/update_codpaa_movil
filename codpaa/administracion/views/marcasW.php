<? session_start(); ?>
<?
if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    include_once('../connexion/bdManager.php');

    ?>

    <div id="marcas_Us" class="easyui-layout" data-options="fit:true">

        <div id="permisoscenter" data-options="region:'center',title:'Resultado',split:true,collapsible:false"
             style="width:100%;height: 100%;background-color: #f3f3f3">


            <div class="row">

                <div class="col-xs-70">
                    <input type="text" name="usuario" style="border:1px solid #ccc; padding: 15px 10px; width: 100%;"
                           id="busqueda_usuario" placeholder="Usuario">
                </div>



                <div class="col-xs-5">
                    <select name="from[]" id="marcas-disponibles" class="multiselect select form-control" multiple="multiple"></select>

                </div>

                <div class="col-xs-2">

                    <button type="button" id="right_All_1" class="btn btn-block action-buttons" onclick="toRight()">Asignar > </button>

                    <button type="button" id="left_Selected_1" class="btn btn-block action-buttons" onclick="deleteAllMar()">remover todas</button>
                    <button type="button" id="left_All_1" class="btn btn-block action-buttons" onclick="toLeft()"> < remover </button>


                </div>

                <div class="col-xs-5">
                    <select name="to[]" id="search-2" size="8" class="form-control select" multiple="multiple"></select>

                </div>


            </div>


        </div>

    </div>



    <script type="application/javascript">

        let idUsuario = 0;

        $('#busqueda_usuario').autocomplete({

            serviceUrl: '../php/search_usuario.php',
            minChars: 2,
            zIndex:9999,
            onSelect: function (suggestions) {

                if (suggestions.data.idUsuario !== undefined){

                    $('#marcas-disponibles').empty();
                    $('#search-2').empty();

                    idUsuario = suggestions.data.idUsuario;

                    showMarcas(idUsuario);
                }


            },
            triggerSelectOnValidInput: false,
            width: 'auto'

        });




        $('#marcas-disponibles').multiselect({
            search: {
                left: '<input type="text" name="q" class="form-control" placeholder="Marcas disponibles" />',
                right: '<input type="text" name="q" class="form-control" placeholder="Marcas" />'
            }

        });

       /* $('#search-2').multiselect({
            search: {
                left: '<input type="text" name="s" class="form-control" placeholder="Marcas asignadas" />'
            }

        });*/


        async function showMarcas(idUsuario){

            $('.action-buttons').prop('disabled', true);
            $.ajax({
                type:'POST',
                dataType:'json',
                url:'../php/usersW/lista_marcasW.php',
                data:{idUsuario:idUsuario},
                success:function(data){

                    $.each(data.marcas, function(i, item){

                        let groupF = $('#marcas-disponibles')
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
                            $('#marcas-disponibles').append(group);

                        }

                        groupF.append(option);

                    } );

                }
            });


            $.ajax({
                type:'POST',
                dataType:'json',
                url:'../php/usersW/marcas_asignadasW.php',
                data:{idUsuario:idUsuario},
                success:function(data){

                    $.each(data.marcas, function(i, item){

                        let groupF = $('#search-2')
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
                            $('#search-2').append(group);

                        }

                        groupF.append(option);
                    } );

                }
            });

            await $('.action-buttons').prop('disabled', false);

        }


        function refreshMarcasAsig(idUsuario){

            $('#search-2').empty();

            $.ajax({
                type:'POST',
                dataType:'json',
                url:'../php/usersW/marcas_asignadasW.php',
                data:{idUsuario:idUsuario},
                success:function(data){

                    $.each(data.marcas, function(i, item){

                        let groupF = $('#search-2')
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
                            $('#search-2').append(group);

                        }

                        groupF.append(option);

                    } );

                }
            });

        }


        function refreshMarcas(idUsuario){

            $('#marcas-disponibles').empty();

            $.ajax({
                type:'POST',
                dataType:'json',
                url:'../php/usersW/lista_marcasW.php',
                data:{idUsuario:idUsuario},
                success:function(data){

                    $.each(data.marcas, function(i, item){

                        let groupF = $('#marcas-disponibles')
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
                            $('#marcas-disponibles').append(group);

                        }

                        groupF.append(option);

                    } );

                }
            });

        }


        function toRight(){
            let selectMarcasDispo = $('#marcas-disponibles');


            if (selectMarcasDispo.val() != null){

                $.ajax({
                    type:'POST',
                    dataType:'json',
                    data:{marcas: selectMarcasDispo.val(), idUsuario:idUsuario},
                    url:'../php/usersW/add_marca.php',
                    success:function(data){


                        let selecctions = data.marcas;

                        for (let i = 0; i < selecctions.length ; i++ ){
                            selectMarcasDispo.find('option[value='+selecctions[i].idMarca+']').remove();
                        }

                        refreshMarcasAsig(idUsuario)



                    }
                });

            }else {
                $.messager.alert('Aviso', 'Marca no seleccionada');

            }
        }


        function toLeft(){


            let selectMarcasDispo = $('#search-2');


            if (selectMarcasDispo.val() != null){

                $.ajax({
                    type:'POST',
                    dataType:'json',
                    data:{marcas: selectMarcasDispo.val(), idUsuario:idUsuario},
                    url:'../php/usersW/delete_marca.php',
                    success:function(data){


                        let selecctions = data.marcas;

                        for (let i = 0; i < selecctions.length ; i++ ){
                            $('#search-2').find('option[value='+selecctions[i].idMarca+']').remove();
                        }

                        refreshMarcas(idUsuario);

                    }
                });

            }else {
                $.messager.alert('Aviso', 'Marca No seleccionada');

            }

        }

        function deleteAllMar(){

            $.messager.alert('Aviso', 'en construccion');

        }







    </script>


<? } else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>