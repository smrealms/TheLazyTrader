<?php
define('FILE_NAME','TheLazyTrader.1.2.8.jar');

header('Content-Type: application/octet-stream');
header('Content-Disposition: attachment; filename="'. FILE_NAME . '"');
readfile(FILE_NAME);
?>