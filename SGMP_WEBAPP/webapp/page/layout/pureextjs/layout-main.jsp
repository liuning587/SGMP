<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/page/layout/pureextjs/include/taglibs.jsp" %>
<html>
<head>
<%@ include file="/page/layout/pureextjs/include/metas.jsp" %>
<title>${title_system}</title>
<%@ include file="/page/layout/pureextjs/include/links.jsp" %>
<style type="text/css">
* { 
    font-family: "宋体"; font-size: 12px!important;
}

h1 {
    float: left;
    margin: 7px;
    font-size: 36px!important;
    font-weight: bolder;
    font-family: "宋体";
}

h2 {
    float: left;
    margin: 16px 10px;
    font-size: 26px!important;
    font-weight: bold;
    font-family: "宋体";
}

p {
    margin: 5px;
}

.function {
    background-image: url(${cxt_webstyle}/img/icons/application_go.png);
}

.selection {
    background-image: url(${cxt_webstyle}/img/icons/application_view_list.png);
}

.setting {
    background-image: url(${cxt_webstyle}/img/icons/folder_wrench.png);
}

.reading {
    background-image: url(${cxt_webstyle}/img/icons/folder_go.png);
}

.cellEditing {
    background-image: url(${cxt_webstyle}/img/icons/page_edit.png);
}
</style>
<%@ include file="/page/layout/pureextjs/include/scripts.jsp" %>
<!--  -->
<script type="text/javascript" src="${ctx_webstatic}/script/pureextjs/module/common.js"></script>
<script type="text/javascript" src="${ctx_webstatic}/script/pureextjs/module/homepage.js"></script>
<script type="text/javascript" src="${ctx_webstatic}/script/pureextjs/module/archivesManagement.js"></script>
<script type="text/javascript" src="${ctx_webstatic}/script/pureextjs/module/parameterManagement.js"></script>
<script type="text/javascript" src="${ctx_webstatic}/script/pureextjs/module/collectionManagement.js"></script>
<script type="text/javascript" src="${ctx_webstatic}/script/pureextjs/module/onlineMonitoring.js"></script>
<script type="text/javascript" src="${ctx_webstatic}/script/pureextjs/module/dataQuery.js"></script>
<script type="text/javascript" src="${ctx_webstatic}/script/pureextjs/module/logQuery.js"></script>
<script type="text/javascript" src="${ctx_webstatic}/script/pureextjs/module/systemManagement.js"></script>
<script type="text/javascript">
Ext.require(['*']);

var activeItemId;
var selectedSoRecord;
var functionMenuTreeStore = '';
var selectionObjectTreeStore = '';
Ext.onReady(function() {
    Ext.QuickTips.init();

    Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));

    var functionContents = [];
    Ext.Object.each(getHomepageFunctions(), function(name, content) {
        functionContents.push(content);
    });
    Ext.Object.each(getArchivesManagementFunctions(), function(name, content) {
        functionContents.push(content);
    });
    Ext.Object.each(getParameterManagementFunctions(), function(name, content){
        functionContents.push(content);
    });
    Ext.Object.each(getCollectionManagementFunctions(), function(name, content){
        functionContents.push(content);
    });
    Ext.Object.each(getOnlineMonitoringFunctions(), function(name, content){
        functionContents.push(content);
    });
    Ext.Object.each(getDataQueryFunctions(), function(name, content){
        functionContents.push(content);
    });
    Ext.Object.each(getLogQueryFunctions(), function(name, content){
        functionContents.push(content);
    });
    Ext.Object.each(getSystemManagementFunctions(), function(name, content){
        functionContents.push(content);
    });

    var contentPanel = {
        id: 'content-panel',
        region: 'center',
        layout: 'card',
        margins: '5 5 5 5',
        activeItem: 0,
        border: false,
        items: functionContents
    };

    functionMenuTreeStore = Ext.create('Ext.data.TreeStore', {
        root: {
            expanded: true
        },
        proxy: {
            type: 'ajax',
            url: '${ctx_webstatic}/customized/project/hd/data/function-menu-tree-data.json',
            timeout: 600000
        }
    });
    var functionMenuTreePanel = Ext.create('Ext.tree.Panel', {
        id: 'function-menu-tree-panel',
        title: '功能菜单区',
        rootVisible: false,
        autoScroll: true,
        store: functionMenuTreeStore
    });
    functionMenuTreePanel.getSelectionModel().on('select', function(selModel, record) {
        if(record.get('leaf')) {
            Ext.getCmp('content-panel').layout.setActiveItem(record.getId() + 'ContentPanel');
            //alert(record.getId());
            if(record.getId() == 'actionTgArchivesManagement') {
                //alert(selectedSoRecord.get('soType'));
                //alert(selectedSoRecord.get('soId'));
                if(selectedSoRecord && selectedSoRecord.get('soType') == 'tg') {
                    //alert(selectedSoRecord.get('soId'));
                    Ext.getCmp('tgInfoForm').getForm().setValues({id: selectedSoRecord.get('soId')});
                }
            }
            else if(record.getId() == 'actionTerminalParameterSetup') {
                initTpsFilterForm();
            }
            else if(record.getId() == 'actionProtectorParameterSetup') {
                initPpsFilterForm();
            }
            else if(record.getId() == 'actionProtectorControlCommandSending') {
                initPccsFilterForm();
            }
            else if(record.getId() == 'actionTgMeterDataReading') {
                initTmdrFilterForm();
            }
            else if(record.getId() == 'actionProtectorDataReading') {
                initPdrFilterForm();
            }
            else if(record.getId() == 'actionAnalogueDataReading') {
                initAdrFilterForm();
            }
            else if(record.getId() == 'actionTgMeterDataQuery') {
                initTmdqFilterForm();
            }
            else if(record.getId() == 'actionProtectorDataQuery') {
                initPdqFilterForm();
            }
            else if(record.getId() == 'actionAnalogueDataQuery') {
                initAdqFilterForm();
            }
            else if(record.getId() == 'actionTgPowerConsumptionQuery') {
                initTpcqFilterForm();
            }
            else if(record.getId() == 'actionTerminalEventsQuery') {
                initTeqFilterForm();
            }
            else if(record.getId() == 'actionProtectorTripEventsQuery') {
                initPteqFilterForm();
            }
        }
    });

    Ext.define('selection-object-treestore-model', {
        extend: 'Ext.data.Model',
        fields: [
                 {name: 'id', type: 'string'},
                 {name: 'text', type: 'string'},
                 {name: 'leaf', type: 'boolean'},
                 {name: 'expanded', type: 'boolean'},
                 {name: 'soType', type: 'string'},
                 {name: 'soId', type: 'string'},
                 {name: 'soName', type: 'string'},
                 {name: 'soOrgId', type: 'string'},
                 {name: 'soTgId', type: 'string'},
                 {name: 'soTermId', type: 'string'},
                 {name: 'soGpId', type: 'string'}
        ],
        idProperty: 'id'
    });
    selectionObjectTreeStore = Ext.create('Ext.data.TreeStore', {
        model: 'selection-object-treestore-model',
        root: {
            expanded: true
        },
        defaultRootId: 'root',
        proxy: {
            type: 'ajax',
            url: ctx_webapp + '/left!getTree.do',
            timeout: 600000,
            reader: {
                type: 'json'
            }
        },
        autoLoad: true
    });
    var selectionObjectTreePanel = Ext.create('Ext.tree.Panel', {
        id: 'selection-object-tree-panel',
        title: '对象选择区',
        rootVisible: false,
        autoScroll: true,
        store: selectionObjectTreeStore
    });
    selectionObjectTreePanel.on('beforeload', function(store, operation, eOpts) {
        //alert(Ext.JSON.encode(operation.node.data));
        operation.params = operation.node.data;
    });
    // 双击事件
    selectionObjectTreePanel.on('itemdblclick', function(treeview, record, item, index, e, eOpts) {
        treeview.toggleOnDblClick = false;
        selectedSoRecord = record;
        activeItemId = Ext.getCmp('content-panel').layout.getActiveItem().id;
        if(activeItemId == 'actionTgArchivesManagementContentPanel') {
            if(selectedSoRecord.get('soType') == 'tg') {
                Ext.getCmp('tgInfoForm').getForm().setValues({id: selectedSoRecord.get('soId')});
            }
        }
        else if(activeItemId == 'actionTgMeterDataQueryContentPanel') {
            Ext.getCmp('tmdq-filter-form').getForm().loadRecord(selectedSoRecord);
            //Ext.getCmp('tmdq-filter-form').getForm().setValues({soId: selectedSoRecord.get('soId'), soOrgId: selectedSoRecord.get('soOrgId')});
        }
    });

    Ext.create('Ext.Viewport', {
        id: 'layout-top',
        layout: 'border',
        items: [
        // create instance immediately
        Ext.create('Ext.Component', {
            region: 'north',
            id: 'panel-north',
            height: 60,
            autoEl: {
                tag: 'div',
                /* html: '<h1>浙江豪顿电气有限公司</h1><h2>低压配电网络信息一体化平台</h2>' */
                html: '<h1>SGMP</h1><h2>Smart Grid Management Platform</h2>'
                /* html: '' */
            }
        }), {
            region: 'west',
            stateId: 'navigation-panel',
            id: 'panel-west',
            title: ' ',
            split: false,
            width: 200,
            minWidth: 200,
            maxWidth: 200,
            collapsible: true,
            animCollapse: true,
            margins: '5 0 5 5',
            layout: 'accordion',
            items: [functionMenuTreePanel, selectionObjectTreePanel]
        }, contentPanel]
    });

    //orgnizationListStore.load();
});
</script>
</head>
<body>
</body>
</html>