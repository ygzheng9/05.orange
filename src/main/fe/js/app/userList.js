layui.use(['jquery', 'layer', 'form', 'table','miniPage','element'], function () {
    const {$, form, table, miniPage, layer, element} = layui;

    // 监听上面的搜索条件
    form.on('submit(data-search-btn)', function (data) {
        const result = JSON.stringify(data.field);

        // 执行搜索重载, where.params 作为 querystring 出给后端
        table.reload('currentTableId', {
            page: {
                curr: 1
            }
            , where: {
                params: result
            }
        });

        return false;
    });

    // 下面的 table 数据
    table.render({
        elem: '#currentTableId',
        url: '/userFind',
        toolbar: '#topToolbar',
        defaultToolbar: ['filter', 'exports', 'print'],
        cols: [[
            {type: "checkbox", width: 50, fixed: "left"},
            {field: 'id', width: 80, title: 'ID', sort: true},
            {field: 'userName', width: 120, title: '用户名', sort: true},
            {field: 'email', width: 150, title: '邮箱', sort: true},
            {field: 'company', width: 150, title: '公司', sort: true},
            {field: 'department', width: 150, title: '部门'},
            {field: 'city', width: 120, title: '城市'},
            {field: 'sex', width: 80, title: '性别'},
            {title: '操作', minWidth: 50, toolbar: '#lineToolbar', fixed: "right", align: "center"}
        ]],
        page: {
            limit: 30,
            limits: [30, 100, 200, 500]
        }
    });

    // 打开 entry form，如果是新增，则空表格；如果是修改，则显示当前记录
    function openEntryForm(url, title) {
        miniPage.getHrefContentA(url)
            .done(function(content) {
                const openWH = miniPage.getOpenWidthHeight();

                const index = layer.open({
                    title: title,
                    type: 1,
                    shade: 0.2,
                    maxmin: true,
                    shadeClose: true,
                    area: [openWH[0] + 'px', openWH[1] + 'px'],
                    offset: [openWH[2] + 'px', openWH[3] + 'px'],
                    content: content,
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });
            });
    }

    //  监听 table 上面的 toolbar 事件
    table.on('toolbar(currentTableFilter)', function (obj) {
        if (obj.event === 'add') {   // 监听添加操作
            openEntryForm('/userEntry?id=0', '添加用户');
        } else if (obj.event === 'delete') {  // 监听删除操作
            const checkStatus = table.checkStatus('currentTableId')
                , data = checkStatus.data;

            const ids = R.map(entry => entry.id, data);

            if (ids.length === 0) {
                layer.alert('未选中任何记录', {
                    title: '警告'
                });
                return;
            }

            layer.confirm('真的删除选中记录吗？', function (index) {
                $.post("/userDeleteBatch", {param: JSON.stringify(ids)}).then(function(res) {
                    if (res.state === 'ok') {
                        table.reload('currentTableId');
                        // 关闭弹出层
                        layer.close(index);
                    } else {
                        const index = layer.alert('批量删除失败，请稍后重试', {
                            title: '操作失败'
                        }, function () {
                            // 关闭弹出层
                            layer.close(index);
                        });
                    }
                });
            });
        }
    });

    // 监听 table 每一行开头的复选框选择
    table.on('checkbox(currentTableFilter)', function (obj) {
        // console.log(obj)
    });

    // 监听 table 每一行的按钮动作
    table.on('tool(currentTableFilter)', function (obj) {
        // console.log("current line: ", obj);

        const data = obj.data;
        if (obj.event === 'edit') {
            const url = '/userEntry?id=' + data.id;
            openEntryForm(url, '编辑用户');

            return false;
        } else if (obj.event === 'delete') {
            layer.confirm('真的删除当前记录吗？', function (index) {
                $.post("/userDelete?id=" + data.id).then(function(res) {
                    layer.close(index);

                    table.reload('currentTableId');
                });
            });
        }
    });
});

