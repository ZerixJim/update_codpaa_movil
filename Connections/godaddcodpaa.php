<?php
# FileName="Connection_php_mysql.htm"
# Type="MYSQL"
# HTTP="true"
$hostname_godaddcodpaa = "50.63.244.75";
$database_godaddcodpaa = "codpaa";
$username_godaddcodpaa = "codpaa";
$password_godaddcodpaa = "Summa12345@";
$godaddcodpaa = mysql_pconnect($hostname_godaddcodpaa, $username_godaddcodpaa, $password_godaddcodpaa) or trigger_error(mysql_error(),E_USER_ERROR); 
?>