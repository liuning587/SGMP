function getParameterManagementFunctions() {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////// 集中器参数设置 ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 集中器信息表单
    var tps_filter_formpanel = Ext.create('Ext.form.Panel', {
        id: 'tps-filter-form',
        region: 'north',
        title: '集中器信息',
        frame: true,
        margins: '5 5 5 5',
        bodyStyle: 'padding: 5px 5px 0 5px;',
        split: false,
        items: [{
            xtype: 'container',
            anchor: '100%',
            layout: 'hbox',
            items: [{
                xtype: 'container',
                layout: 'anchor',
                flex: 1,
                width: 240,
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '所属机构',
                    labelWidth: 60,
                    name: 'orgName'
                }]
            }, {
                xtype: 'container',
                layout: 'anchor',
                flex: 1,
                width: 240,
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '台区名称',
                    labelWidth: 60,
                    name: 'tgName'
                }]
            }, {
                xtype: 'container',
                layout: 'anchor',
                flex: 1,
                width: 240,
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '逻辑地址',
                    labelWidth: 60,
                    name: 'logicalAddr'
                }]
            }]
        }]/*,
        buttonAlign: 'center',
        buttons: [{
            text: '查询',
            handler: function() {
                
            }
        }]*/
    });
    // 集中器参数列表数据源
    Ext.define('tps-termparam-gridstore-model', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'SN', type: 'string'},                          /* 序号 */
            {name: 'P_CODE', type: 'string'},                   /* 参数编码 */
            {name: 'P_NAME', type: 'string'},                   /* 参数名称 */
            {name: 'P_VALUE', type: 'string'},                  /* 参数值 */
            {name: 'OP_RESULT', type: 'string'}                 /* 操作结果 */
        ],
        idProperty: 'P_CODE'
    });
    var tps_termparam_gridstore = Ext.create('Ext.data.Store', {
        // destroy the store if the grid is destroyed
        //autoDestroy: true,
        model: 'tps-termparam-gridstore-model',
        //remoteSort: true,
        proxy: {
            // load using script tags for cross domain, if the data in on the same domain as
            // this page, an HttpProxy would be better
            type: 'ajax',
            url: ctx_webstatic + '/customized/project/hd/data/tps-termparam-grid-data.json',
            reader: {
                type: 'json',
                root: 'records',
                totalProperty: 'count'
            },
            // sends single sort as multi parameter
            simpleSortMode: true
        },
        /*sorters: [{
            property: 'P_CODE',
            direction: 'ACS'
        }],*/
        // 
        autoLoad: true
    });
    // 集中器参数列表
    var tps_termparam_grid_selections = '';
    var tps_termparam_grid_selmodel = Ext.create('Ext.selection.CheckboxModel', {
        listeners: {
            selectionchange: function(sm, selections) {
                tps_termparam_gridpanel.down('#tps-termparam-setting-button').setDisabled(selections.length == 0);
                tps_termparam_gridpanel.down('#tps-termparam-reading-button').setDisabled(selections.length == 0);
                tps_termparam_grid_selections = selections;
            }
        }
    });
    var tps_termparam_gridpanel = Ext.create('Ext.grid.Panel', {
        id: 'tps-termparam-grid',
        title: '集中器参数',
        xtype: 'grid',
        layout: 'fit',
        store: tps_termparam_gridstore,
        loadMask: true,
        columns: [
            {text: "序号", width: 80, sortable: true, dataIndex: 'SN'},
            {text: "参数编码", width: 120, sortable: true, dataIndex: 'P_CODE'},
            {text: "参数名称", width: 200, sortable: true, dataIndex: 'P_NAME'},
            {text: "参数值", width: 200, sortable: true, dataIndex: 'P_VALUE'},
            {text: "操作结果", flex: 1, sortable: true, dataIndex: 'OP_RESULT'}
        ],
        columnLines: true,
        selModel: tps_termparam_grid_selmodel,
        // inline buttons
        dockedItems: [{
            xtype: 'toolbar',
            dock: 'top',
            items: [{
                itemId: 'tps-termparam-setting-button',
                text: '设置',
                tooltip: '设置集中器参数',
                iconCls: 'setting',
                disabled: true,
                handler: function() {
                    //alert('设置集中器参数');
                    var records = tps_termparam_grid_selections;
                    if(typeof(records) == "undefined" || records.length == 0) {
                        Ext.MessageBox.alert('提示', '请选择要设置的参数项', function(btn) {
                            return;
                        });
                    }
                    else {
                        var paramsAndValues = '';
                        for(var i = 0; i < records.length; i++) {
                            paramsAndValues += records[i].get("P_CODE") + ':' + records[i].get("P_VALUE") + ';';
                        }
                        Ext.Ajax.request({
                            url: ctx_webapp + '/pm/tps!send.do',
                            params: {
                                action: 'setting',
                                logicalAddr: '96123452',
                                paramsAndValues: paramsAndValues
                            },
                            method: 'POST',
                            success: function(response) {
                                //alert(response.responseText);
                            },
                            failure: function(response) {
                                //alert(response.responseText);
                            }
                        });
                        //alert(paramsAndValues);
                        /*Ext.MessageBox.show({
                            title: '正在等待',
                            msg: 'Loading items...',
                            progressText: 'Initializing...',
                            width:300,
                            progress:true,
                            closable:false,
                            animateTarget: 'mb6'
                        });

                        // this hideous block creates the bogus progress
                        var f = function(v){
                             return function(){
                                 if(v == 12){
                                     Ext.MessageBox.hide();
                                     Ext.example.msg('Done', 'Your fake items were loaded!');
                                 }else{
                                     var i = v/11;
                                     Ext.MessageBox.updateProgress(i, Math.round(100*i)+'% completed');
                                 }
                            };
                        };
                        for(var i = 1; i < 13; i++){
                            setTimeout(f(i), i*500);
                        }*/
                    }
                }
            }, '-', {
                itemId: 'tps-termparam-reading-button',
                text: '读取',
                tooltip: '读取集中器参数',
                iconCls: 'reading',
                disabled: true,
                handler: function() {
                    //alert('读取集中器参数');
                    var records = tps_termparam_grid_selections;
                    if(typeof(records) == "undefined" || records.length == 0) {
                        Ext.MessageBox.alert('提示', '请选择要设置的参数项', function(btn) {
                            return;
                        });
                    }
                    else {
                        var paramsAndValues = '';
                        for(var i = 0; i < records.length; i++) {
                            paramsAndValues += records[i].get("P_CODE") + ":" + ';';
                        }
                        Ext.Ajax.request({
                            url: ctx_webapp + '/pm/tps!send.do',
                            params: {
                                action: 'reading',
                                logicalAddr: '96123452',
                                paramsAndValues: paramsAndValues
                            },
                            method: 'POST',
                            success: function(response) {
                                //alert(response.responseText);
                            },
                            failure: function(response) {
                                //alert(response.responseText);
                            }
                        });
                        //alert(paramsAndValues);
                        /*Ext.MessageBox.show({
                            title: '正在等待',
                            msg: 'Loading items...',
                            progressText: 'Initializing...',
                            width:300,
                            progress:true,
                            closable:false,
                            animateTarget: 'mb6'
                        });

                        // this hideous block creates the bogus progress
                        var f = function(v) {
                             return function() {
                                 if(v == 12) {
                                     Ext.MessageBox.hide();
                                     Ext.example.msg('Done', 'Your fake items were loaded!');
                                 }
                                 else {
                                     var i = v/11;
                                     Ext.MessageBox.updateProgress(i, Math.round(100*i)+'% completed');
                                 }
                            };
                        };
                        for(var i = 1; i < 13; i++) {
                            setTimeout(f(i), i*500);
                        }*/
                    }
                }
            }]
        }]
    });
    // 测量点参数列表数据源
    Ext.define('tps-gpparam-gridstore-model', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'SN', type: 'int'},                          /* 序号 */
            {name: 'P_CODE', type: 'string'},                   /* 参数编码 */
            {name: 'P_NAME', type: 'string'},                   /* 参数名称 */
            {name: 'P_VALUE', type: 'string'},                  /* 参数值 */
            {name: 'OP_RESULT', type: 'string'}                 /* 操作结果 */
        ],
        idProperty: 'P_CODE'
    });
    var tps_gpparam_gridstore = Ext.create('Ext.data.Store', {
        // destroy the store if the grid is destroyed
        //autoDestroy: true,
        model: 'tps-gpparam-gridstore-model',
        remoteSort: true,
        proxy: {
            // load using script tags for cross domain, if the data in on the same domain as
            // this page, an HttpProxy would be better
            type: 'ajax',
            url: ctx_webapp + '/pm/tps!queryGpParamGrid.do',
            reader: {
                type: 'json',
                root: 'records',
                totalProperty: 'count'
            },
            method: 'POST',
            // sends single sort as multi parameter
            simpleSortMode: true
        }/*,
        sorters: [{
            property: 'P_CODE',
            direction: 'ACS'
        }]*//*,
        autoLoad: true*/
    });
    // 测量点参数列表
    var tps_gpparam_grid_selmodel = Ext.create('Ext.selection.CheckboxModel', {
        listeners: {
            selectionchange: function(sm, selections) {
                tps_gpparam_gridpanel.down('#tps-gpparam-setting-button').setDisabled(selections.length == 0);
                tps_gpparam_gridpanel.down('#tps-gpparam-reading-button').setDisabled(selections.length == 0);
            }
        }
    });
    var tps_gpparam_gridpanel = Ext.create('Ext.grid.Panel', {
        id: 'tps-gpparam-grid',
        title: '测量点参数',
        xtype: 'grid',
        layout: 'fit',
        store: tps_gpparam_gridstore,
        loadMask: true,
        columns: [
            {text: "序号", width: 80, sortable: true, dataIndex: 'SN'},
            {text: "参数编码", width: 120, sortable: true, dataIndex: 'P_CODE'},
            {text: "参数名称", width: 200, sortable: true, dataIndex: 'P_NAME'},
            {text: "参数值", width: 200, sortable: true, dataIndex: 'P_VALUE'},
            {text: "操作结果", flex: 1, sortable: true, dataIndex: 'OP_RESULT'}
        ],
        columnLines: true,
        selModel: tps_gpparam_grid_selmodel,
        // inline buttons
        dockedItems: [{
            xtype: 'toolbar',
            dock: 'top',
            items: [{
                itemId: 'tps-gpparam-setting-button',
                text: '设置',
                tooltip: '设置测量点参数',
                iconCls: 'setting',
                disabled: true,
                handler: function() {
                    //alert('设置测量点参数');
                }
            }, '-', {
                itemId: 'tps-gpparam-reading-button',
                text: '读取',
                tooltip: '读取测量点参数',
                iconCls: 'reading',
                disabled: true,
                handler: function() {
                    //alert('读取测量点参数');
                }
            }, '->', {
                xtype: 'combobox',
                name: 'gpsn',
                fieldLabel: '测量点',
                store: Ext.create('Ext.data.Store', {
                    fields: ['value', 'label'],
                    data: [
                        {"value": 1, "label": "测量点 1"},
                        {"value": 2, "label": "测量点 2"},
                        {"value": 3, "label": "测量点 3"},
                        {"value": 4, "label": "测量点 4"},
                        {"value": 5, "label": "测量点 5"},
                        {"value": 6, "label": "测量点 6"},
                        {"value": 7, "label": "测量点 7"},
                        {"value": 8, "label": "测量点 8"}
                    ]
                }),
                valueField: 'value',
                displayField: 'label',
                queryMode: 'local',
                forceSelection : true,
                triggerAction : 'all',
                editable: false,
                labelWidth: 50,
                width: 130,
                minWidth: 130,
                maxWidth: 130,
                value: 1,
                listeners: {
                    change: function(combo, newValue, oldValue, eOpts) {
                        alert(newValue);
                    }
                }
            }]
        }]
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 集中器参数设置
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 保护器信息表单
    var pps_filter_formpanel = Ext.create('Ext.form.Panel', {
        id: 'pps-filter-form',
        region: 'north',
        title: '保护器信息',
        frame: true,
        margins: '5 5 5 5',
        bodyStyle: 'padding: 5px 5px 0 5px;',
        split: false,
        items: [{
            xtype: 'container',
            anchor: '100%',
            layout: 'hbox',
            items: [{
                xtype: 'container',
                layout: 'anchor',
                flex: 1,
                width: 240,
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '所属机构',
                    labelWidth: 60,
                    name: 'orgName'
                }]
            }, {
                xtype: 'container',
                layout: 'anchor',
                flex: 1,
                width: 240,
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '台区名称',
                    labelWidth: 60,
                    name: 'tgName'
                }]
            }, {
                xtype: 'container',
                layout: 'anchor',
                flex: 1,
                width: 240,
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '保护器名称',
                    labelWidth: 72,
                    name: 'psName'
                }]
            }]
        }]/*,
        buttonAlign: 'center',
        buttons: [{
            text: '查询',
            handler: function() {
                
            }
        }]*/
    });

    return {
        /*
         * ==================== |集中器参数设置| ====================
         */
        actionTerminalParameterSetup: {
            xtype: 'tabpanel',
            id: 'actionTerminalParameterSetupContentPanel',
            activeTab: 0,
            defaults: {bodyStyle: 'padding: 0'},
            items: [{
                title: '集中器参数设置',
                layout: 'border',
                items: [tps_filter_formpanel, {
                    xtype: 'tabpanel',
                    plain: true,
                    region: 'center',
                    margins: '0 5 5 5',
                    activeTab: 0,
                    items: [tps_termparam_gridpanel, tps_gpparam_gridpanel]
                }]
            }]
        },

        /*
         * ================  保护器参数设置  =======================
         */
        actionProtectorParameterSetup: {
            xtype: 'tabpanel',
            id: 'actionProtectorParameterSetupContentPanel',
            activeTab: 0,
            defaults: {bodyStyle: 'padding: 0'},
            items: [{
                title: '保护器参数设置',
                layout: 'border',
                items: [pps_filter_formpanel, {
                    xtype: 'tabpanel',
                    plain: true,
                    region: 'center',
                    margins: '0 5 5 5',
                    activeTab: 0,
                    items: [{
                        title: '保护器参数',
                        html: '<p>保护器参数</p>'
                    }]
                }]
            }]
        }
    };
}