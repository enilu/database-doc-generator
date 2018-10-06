# database-doc-generator
数据库文档生成器

- 该工具根据给定的链接生成数据库文档，如果你嫌powerdesigner太重，那么可以试试该工具。
- 你可以克隆该项目后运行mvn package打包，然后运行发布报中bin/start.bat
- 运行程序后按照下面提示输入对应数据库参数：

```bash
input mysql host:
127.0.0.1
input mysql port:
3306
input database name:
guns-lite
input mysql username:
root
input mysql password:
root
```
- 输入完成后回车，即可生成数据库文档目录${dbname}-doc,目录中文档以markdown文件为载体：

![doc](doc.jpg)

- 确保安装了gitbook后，进入上述文件目录的命令行窗口运行：gitbook serve
```bash
E:\\database-doc-generator-20181006100721\guns-lite-doc>gitbook serve
openssl config failed: error:02001003:system library:fopen:No such process
Live reload server started on port: 35729
Press CTRL+C to quit ...

info: 7 plugins are installed
info: loading plugin "livereload"... OK
info: loading plugin "highlight"... OK
info: loading plugin "search"... OK
info: loading plugin "lunr"... OK
info: loading plugin "sharing"... OK
info: loading plugin "fontsettings"... OK
info: loading plugin "theme-default"... OK
info: found 15 pages
info: found 0 asset files
info: >> generation finished with success in 1.6s !

Starting server ...
Serving book on http://localhost:4000
```
- 访问http://localhost:4000，即可在线查看数据库文档

![summary](summary.jpg)

![table](table.jpg)