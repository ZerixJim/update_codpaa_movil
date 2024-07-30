<?php

session_start();

if(isset($_SESSION['idUser'])){ ?>

<header>
    <link rel="stylesheet" href="../css/exhibicion.css">
</header>

    <div class="content-padding">

        <div id="contenedor_solicitud">

            <div id="contenedor-formulario">
                
                <h1>
                    Alta Exhibiciones
                </h1>

                <h3>Formulario para dar de alta exhibiciones </h3>


                <form id="fm_solicitud">

                    <ul>
                        <li>
                            <label for="name">Nombre de la exhibición: </label>
                            <input id="name" type="text" name="name" required placeholder="Nombre de exhibición" maxlength="50">
                            <span id="span-tittle" class="span-hint"></span>
                        </li>
                       
                        <li>

                            <label for="cliente">Cliente: </label>
                            <input type="text" class="easyui-combobox" url="../php/get_cliente.php"
                                   data-options="valueField:'idCliente', textField:'razonsocial', required:true" id="cliente">

                        </li>

<!--
                        <li>

                            <label for="calendar">Fecha deseable de entrega</label>
                            <input class="easyui-datebox" id="calendar" data-options="formatter:myformatter,parser:myparser, required:true"/>

                        </li>
-->

                        <li id="button_sub">
                            <button class="submit"  id="button_submit">Guardar Exhibición</button>
                        </li>

                    </ul>

                </form>

            </div>

        </div>

    </div>



    <script type="text/javascript">

        let files;

        $(document).ready(function(){

            //let calendar = $('#calendar');
            
            $('#button_submit').on('click', function(){
                enviarFormulario();
            });

            $('#nombre').keyup(function(){

                if($(this).val() == null){

                    $('#span-tittle').html('Nombre de exhibición muy corto');

                    $(this).addClass('hint');

                }else {
                    $('#span-tittle').html('');

                    $(this).removeClass('hint');
                }

            });


            $('#Cliente').keyup(function(){

                if ($(this).val() == null){

                    $('#span-desc').html('Seleccione un cliente');

                    $(this).addClass('hint');

                }else {
                    $('#span-desc').html('');
                    $(this).removeClass('hint');
                }


            });

            $('#fm_solicitud').submit(function(event){
                event.preventDefault();
            });

        });

        function enviarFormulario(){

            let nombre  = $('#name');
            let cliente = $('#cliente');
            //console.log(nombre.val());
            //console.log(cliente.val());
            //let tipo = $('#tipo');


            if (nombre.val() !== null && cliente.val() !== null){


                if($.isNumeric(cliente.combobox('getValue')) > 0){

                        let formData = new FormData();

                        formData.append('nombre', nombre.val());
                        formData.append('cliente', cliente.val());
                        //formData.append('fecha', fecha.datebox('getValue'));

                        $.ajax({
                            type:'POST',
                            /*
                            cambiar ruta de aqui
                            */
                            url:'../php/insert_exhibicion.php',
                            data: formData,
                            dataType:'json',
                            cache:false,
                            processData: false,
                            contentType:false,

                            beforeSend: function () {

                                $("#button_submit").attr("disabled", true);
                                $("<img src='../../imagenes/loaders/gears-2.gif' " +
                                    " id='loading-imagen' class='loadin-image' />").appendTo("#contenedor_solicitud");
                            },
                            success:function(data){

                                nombre.val('');
                                cliente.combobox('clear');
                                //tipo.combobox('clear');
                                //let title2 = data.title;
                                
                                $.messager.confirm('Aviso', 'Exhibición guardada', 'info');

                            },
                            error:function(jqxhr, textStatus, errorThrown){

                                $.messager.alert('Aviso', jqxhr.responseText, 'error');
                            },
                            complete: function (jqXHR, status) {

                                $("#loading-imagen").remove();
                                $("#button_submit").attr("disabled", false);

                            }
                        });

                }else {
                    $.messager.alert('Aviso', 'Cliente no válido', 'warning');
                }

            }else {
                $.messager.alert('Aviso', 'Formato no válido', 'warning');
                console.log('form is not valid');
            }

        }

    </script>



<?php }else{
        echo 'no has iniciado sesion';
        header('refresh:2,../index.php');
}

?>
