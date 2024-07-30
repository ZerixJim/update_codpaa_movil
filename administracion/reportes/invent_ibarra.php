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

    <div title="Inventarios" id="invent_ib" closable="true" > 

<iframe 
src="https://docs.google.com/spreadsheets/d/1OOIX4LKJCZEFWVka4jypSFwgtUqWbjiVx3JIJAgSZ0I/pubhtml?gid=897626834&amp;single=true&amp;widget=true&amp;headers=false" width="100%" height="600px"></iframe>


    </div>

<? }else{}



}else{

    echo "valor= ".$_SESSION['idUser'];

}



?>







