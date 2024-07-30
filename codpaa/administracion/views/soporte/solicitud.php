<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 21/04/2017
 * Time: 10:46 AM
 */

ob_start();
session_start();


if(isset($_SESSION['idUser'])){


    ?>

    <style type="text/css">

        *:focus{
            outline: none;
        }


        #contenedor_solicitud{
            padding: 20px;
            position: relative;
        }

        #contenedor_solicitud h1{
            color: #585858;
        }

        #contenedor_solicitud h3{
            margin: 20px;
            color: #8e8e8e;
        }

        #fm_solicitud{
            margin-top: 50px;
        }


        #fm_solicitud ul{

            list-style: none outside;
            margin: 0;
            padding: 0;
        }

        #fm_solicitud li{
            padding: 12px;
            border-bottom: 1px solid #eee;
            position: relative;
            color: #8e8e8e;
        }

        #fm_solicitud li:last-child {
            border:none;
        }

        #fm_solicitud label{
            width: 30%;
            float: left;
            text-align: left;
            display: inline-block;
        }

        #fm_solicitud input, #fm_solicitud textarea{
            border: 1px solid #ccc;
            min-height: 20px;
            min-width: 220px;
            padding: 5px 8px;

            transition: all 0.3s ease-out;
        }


        button.submit{
            background-color: #68b12f;
            border: 1px solid #509111;
            border-bottom: 1px solid #5b992b;
            border-radius: 3px;
            -webkit-border-radius: 3px;
            -moz-border-radius: 3px;
            -ms-border-radius: 3px;
            -o-border-radius: 3px;
            color: white;
            font-weight: bold;
            padding: 16px 20px;
            text-align: center;
            text-shadow: 0 -1px 0 #396715;
        }

        #button_sub{
            text-align: right;
        }

        button.submit:hover{
            cursor: pointer;
        }

       /* #fm_solicitud input:focus, #fm_solicitud textarea:focus{
            border: 1px solid #0b93d5;
            background-color: #d7f1ff;
        }*/



        #fm_solicitud input, #fm_solicitud textarea {

            /* box-shadow: 0 0 3px #ccc, 0 10px 15px #eee inset;*/
            border-radius: 2px;
            padding-right: 30px;
            -moz-transition: all .25s;
            -webkit-transition: all .25s;
            -o-transition: all .25s;
            transition: all .25s;
            /*transition: padding .25s;*/
        }

        input[type=file]{
            border: 0 none;
        }

        ::-webkit-input-placeholder { /* Chrome/Opera/Safari */
            color: #bdbdbd;
        }
        ::-moz-placeholder { /* Firefox 19+ */
            color: #bdbdbd;
        }
        :-ms-input-placeholder { /* IE 10+ */
            color: #bdbdbd;
        }
        :-moz-placeholder { /* Firefox 18- */
            color: #bdbdbd;
        }

        .span-hint{

            color: indianred;
            padding: 5px;

        }


        input.hint{

            font-weight: bold;

            border: 1px solid #d52715;
            background-color: #fff3f3;
        }

        textarea.hint{

            font-weight: bold;

            background-color: #fff3f3;
        }

    </style>


    <div class="content-padding">

        <div id="contenedor_solicitud">

            <div id="contenedor-formulario">

                <h1>
                    Reportes / Solicitudes
                </h1>

                <h3>En esté formulario se podran hacer solicitudes y Reportar errores </h3>


                <form id="fm_solicitud" method="post">

                    <ul>
                        <li>
                            <label for="title">Titulo de solicitud</label>
                            <input id="title" type="text" name="title" required placeholder="titulo de la solicitud" maxlength="50">
                            <span id="span-tittle" class="span-hint"></span>
                        </li>
                        <li>
                            <label for="descripcion">Descripcion</label>
                            <textarea rows="6" cols="50" id="descripcion" required placeholder="descripción" name="description" maxlength="255"></textarea>
                            <span id="span-desc" class="span-hint"></span>

                        </li>

                        <li>

                            <label for="tipo">Tipo Solicitud</label>
                            <input type="text" class="easyui-combobox" url="../php/soporte/get_tipos.php"
                                   data-options="valueField:'id', textField:'text', required:true"
                                   id="tipo">

                        </li>
                        <!--<li>
                            <label for="para">Solicitado para</label>
                            <input type="text" id="para" class="easyui-combobox" required prompt="Departamento">

                        </li>-->

                        <li>

                            <label for="calendar">Fecha deseable de entrega</label>
                            <input class="easyui-datebox" id="calendar" data-options="formatter:myformatter,parser:myparser, required:true"/>

                        </li>

                        <li>
                            <label for="archivo">Adjuntar archivo</label>
                            <input id="archivo" type="file" name="archivo" multiple>
                        </li>



                        <li id="button_sub">


                            <button class="submit" type="submit" id="button_submit">Enviar Solicitud</button>

                        </li>



                    </ul>


                </form>


            </div>





        </div>


    </div>



    <script type="text/javascript">


        let files;
        $(document).ready(function(){


            let calendar = $('#calendar');
            $('#button_submit').on('click', function(){

                enviarFormulario();

            });

            $('#title').keyup(function(){

                if($(this).val().length < 4){

                    $('#span-tittle').html('Titulo muy corto');

                    $(this).addClass('hint');

                }else {
                    $('#span-tittle').html('');

                    $(this).removeClass('hint');
                }

            });


            $('#descripcion').keyup(function(){

                if ($(this).val().length < 15){

                    $('#span-desc').html('Descripcion muy corta');

                    $(this).addClass('hint');

                }else {
                    $('#span-desc').html('');
                    $(this).removeClass('hint');
                }


            });


            $('#fm_solicitud').submit(function(event){
                event.preventDefault();
            });

            $('#archivo').on('change', prepareUpload);


            calendar.datebox({


                onChange:function(newValue, oldValue){


                    if (isValidDate(newValue)){

                        if(!validateDate(newValue)){
                            $(this).datebox('clear');
                        }

                    }


                }

            }).datebox('calendar').calendar({
                validator:function(date){
                    let now = new Date();
                    let d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1);
                    let d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 10);


                    return date >= d1;
                }
            });


        });


        function validateDate(date){

            let dateParts = date.split("-");

            let format = new Date(dateParts[0], dateParts[1] - 1 , dateParts[2]);

            let now = new Date();
            let d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1);


            return format >= d1;
        }

        function myformatter(date){
            let y = date.getFullYear();
            let m = date.getMonth()+1;
            let d = date.getDate();
            return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
        }
        function myparser(s){
            if (!s) return new Date();
            let ss = (s.split('-'));
            let y = parseInt(ss[0],10);
            let m = parseInt(ss[1],10);
            let d = parseInt(ss[2],10);
            if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
                return new Date(y,m-1,d);
            } else {
                return new Date();
            }
        }

        function isValidDate(dateString) {
            let regEx = /^\d{4}-\d{2}-\d{2}$/;
            return dateString.match(regEx) != null;
        }





        function prepareUpload(event){

            files = event.target.files;

           //console.log("numero de archivos" + files.length);

        }



        function enviarFormulario(){

            let title  = $('#title');
            let descrip = $('#descripcion');
            let fecha = $('#calendar');
            let tipo = $('#tipo');


            if (title.val().length > 5 && descrip.val().length > 15){


                if($.isNumeric(tipo.combobox('getValue')) && tipo.combobox('getValue') > 0){

                    if(isValidDate(fecha.datebox('getValue'))){

                        let formData = new FormData();

                        formData.append('title', title.val());
                        formData.append('description', descrip.val());
                        formData.append('fecha', fecha.datebox('getValue'));
                        formData.append('tipo', tipo.combobox('getValue'));


                        if(files !== undefined){
                            if(files.length > 0){

                                //formData.append('file', files);

                                $.each(files, function (index, value) {

                                    formData.append('file[]', value);

                                    //console.log("v" + index);
                                });

                            }
                        }


                        $.ajax({
                            type:'POST',
                            url:'../../php/soporte/solicitud.php',
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

                                let fechaMail = fecha.datebox('getValue');


                                title.val('');
                                descrip.val('');
                                fecha.datebox('clear');
                                tipo.combobox('clear');
                                let title2 = data.title;
                                $('#archivo').val('');


                                $.messager.alert('Aviso', 'Solicitud enviada', 'info');

                                $.ajax({
                                    url:'../../php/mail/send_email.php',
                                    data:{
                                        to:'gustavo.ibarra@plataformavanguardia.com',
                                        status:'Creado',
                                        fecha:fechaMail,
                                        comentario:'listo!!',
                                        title:title2
                                    },
                                    success:function (data) {

                                    },
                                    error:function(jqxhr, textStatus, errorThrown){

                                    }

                                });


                            },
                            error:function(jqxhr, textStatus, errorThrown){

                                $.messager.alert('Aviso', jqxhr.responseText, 'error');
                                //console.log(textStatus + ' ' + errorThrown);
                            },
                            complete: function (jqXHR, status) {

                                $("#loading-imagen").remove();
                                $("#button_submit").attr("disabled", false);


                            }
                        });




                    }else {
                        $.messager.alert('Aviso', 'Fecha no valida', 'warning');
                    }



                }else {
                    $.messager.alert('Aviso', 'Tipo de solicitud no valido', 'warning');
                }



            }else {

                console.log('form is not valid');
            }

        }

    </script>


    <?


}else{
    echo "acceso denegado";
}