<?php

/**

 * Created by PhpStorm.

 * User: Gustavo

 * Date: 3/09/14

 * Time: 10:13

 */

ob_start();

session_start();


if(isset($_SESSION['idUser']) && isset($_SESSION['permiso'])){



    if($_SESSION['permiso'] >= 1){ ?>

    <div title="Inteligencia de Mercado" id="im_ib" closable="true">

<iframe 
src="https://docs.google.com/spreadsheets/d/1FGfU3zwHRYxfV40FkALQbkUjlf3ANv9hcGAz67aSTRI/pubhtml?gid=1826072726&amp;single=true&amp;widget=true&amp;headers=false" width="100%" height="600px"></iframe>


    </div>

<? }else{}



}else{

    echo "valor= ".$_SESSION['idUser'];

}



?>







