<?php
ob_start();
session_start();


/**
 * Created by PhpStorm.
 * User: grim
 * Date: 12/06/2018
 * Time: 01:15 PM
 */

?>


<div id="dashboard"  class="flex-center" style="width: 100%;height: 100%;padding: 50px;box-sizing: border-box;">

    <div id="contenido_bienvenido" style="margin-bottom: 50px;">

        <?php

        $nombre = array();
        $nombre = explode(' ', $_SESSION['usuario']);


        ?>

        <div id="welcome">
            <h1> Buen día
                <span style="text-transform: uppercase;"><?= $nombre[0]; ?></span>
            </h1>

            <h2 style="color: #999999">Te damos la bienvenida a la interfaz de Codpaa</h2>

        </div>



    </div>


    <div id="history-content" style="display: none;">

        <h2>Módulos Recientes</h2>

        <ul class="list" style="margin-top: 25px;"></ul>

    </div>





</div>


<script>




    function loadHistory() {

        let keys = Object.keys(localStorage);

        if (keys.length > 0){

            $('#history-content').show();


            keys.forEach((value, index) => {


                if (value !== 'userid'){

                    let data = JSON.parse(localStorage.getItem(value));

                    $('#history-content .list')
                        .append(`<li onclick="clickElement(this)" data-title="${data.title}" data-url="${data.href}" data-icon="${data.image}">


                                <div class="image-icon">
                                    <img alt="image" src="../imagenes/icons/${data.image}.png" style="width: 32px; height: auto;">
                                </div>

                                <br/>

                                ${data.title}

                        </li>`);
                }



            });

        }




    }


    function clickElement(element) {

        let $this = $(element);

        //console.log($this.data("title") + " " + $this.data("url"));

        addTapMen($this.data("url"), $this.data("title"), $this.data("icon"));

    }



   /* setTimeout(()=>{

        $('#contenido_bienvenido').fadeOut("fast");



    }, 2000);*/



    loadHistory();


</script>




