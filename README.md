# Лабораторная работа №1

## Описание
Для выполнения лабораторной работы выбрана СУБД PostgreSQL.
Созданы три базы данных:
* projects_db - хранит информацию о работниках, проектах и ролях в проектах
* resumes_db - хранит информацию о работниках, их заработной плате, премиях и навыках
* data_warehouse - хранилище данных (ХД), объединяющее данные из двух источников

Структура баз данных представлена с помощью [ER диаграмм](ER%20диаграммы.pdf).
Также представлено описание алгоритма по сбору и объединению данных из этих БД в третью 
(ХД) с помощью [BPMN](BPMN.png) в редакторе Camunda modeler.

В среде разработки DataGrip были созданы следущие SQL-скрипты:
  * [create_databases](create_databases.sql) - создание трёх БД
  * [create_tables_projects_db](create_tables_projects_db.sql) - создание таблиц в projects_db и заполнение их данными
  * [create_tables_resume_db](create_tables_resume_db.sql) - создание таблиц в resumes_db и заполнение их данными
  * [create_tables_data_warehouse](create_tables_data_warehouse.sql) - создание таблиц в data_warehouse
  * [union_data_to_dw](union_data_to_dw.sql) - получение данных из projects_db и resumes_db, нормализация данных и записывание их в data_warehouse

