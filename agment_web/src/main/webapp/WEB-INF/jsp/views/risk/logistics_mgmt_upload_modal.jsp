<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/3/28
  Time: 15:55
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal-header">
    <h3 class="modal-title" id="modal-title">订单批量处理</h3>
</div>
<div class="modal-body" id="modal-body">
    <div class="ibox-content no-padding no-borders">
        <div class="sk-spinner sk-spinner-wave">
            <div class="sk-rect1"></div>
            <div class="sk-rect2"></div>
            <div class="sk-rect3"></div>
            <div class="sk-rect4"></div>
            <div class="sk-rect5"></div>
        </div>

        <div class="panel panel-danger">
            <div class="panel-heading">
                <i class="fa fa-warning"></i>&nbsp;&nbsp;注意事项！
            </div>
            <div class="panel-body">
                <ul>
                    <li>
                        <strong>批量操作为不可逆操作！</strong>
                    </li>
                    <li>
                        <strong>文件必须为&nbsp;<code>.xls</code>&nbsp;<code>.xlsx</code>&nbsp格式！</strong>
                    </li>
                    <li>
                        <strong>Excel必须包含列名(不分先后顺序，可含其他无关列)：<code>平台订单号</code>&nbsp;<code>商户订单号</code>&nbsp;<code>快递公司</code>&nbsp;<code>快递单号</code>&nbsp;四列</strong>
                    </li>
                    <li>
                        <strong>单文件上传大小最大限制10&nbsp;MB</strong>
                    </li>
                </ul>
            </div>
        </div>

        <div class="hr-line-dashed"></div>

        <div class="table-responsive">
            <table class="table table-condensed table-striped table-hover" style="height: 68px">
                <thead>
                <tr>
                    <th class="text-center" style="line-height: 1">文件名</th>
                    <th class="text-center" style="line-height: 1">大小</th>
                    <th class="text-center" style="line-height: 1">进度</th>
                    <th class="text-center" style="line-height: 1">状态</th>
                    <th class="text-center" style="line-height: 1">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in uploader.queue">
                    <td class="text-center" style="line-height: 1">
                        <strong>{{item.file.name}}</strong>
                    </td>
                    <td class="text-center" style="line-height: 1" nowrap>
                        {{item.file.size/1024/1024 | number : 2}} MB
                    </td>
                    <td class="text-center" style="line-height: 1">
                        <div class="progress" style="margin-bottom: 0;">
                            <div class="progress-bar" role="progressbar"
                                 ng-style="{'width': item.progress + '%'}">
                                <span class="ng-binding ng-scope">{{item.progress + '%'}}</span>
                            </div>
                        </div>
                    </td>
                    <td class="text-center" style="line-height: 1">
                        <span ng-show="item.isSuccess"><i class="fa fa-check"></i></span>
                        <span ng-show="item.isCancel"><i class="fa fa-ban"></i></span>
                        <span ng-show="item.isError"><i class="fa fa-times"></i></span>
                    </td>
                    <td class="text-center" style="line-height: 1" nowrap>
                        <button type="button" class="btn btn-success btn-xs" ng-click="item.upload()"
                                ng-disabled="item.isReady || item.isUploading || item.isSuccess">
                            <i class="fa fa-upload"></i>&nbsp;上传
                        </button>
                        <button type="button" class="btn btn-warning btn-xs" ng-click="item.cancel()"
                                ng-disabled="!item.isUploading">
                            <i class="fa fa-ban"></i>&nbsp;取消
                        </button>
                        <button type="button" class="btn btn-danger btn-xs" ng-click="item.remove()">
                            <i class="fa fa-trash"></i>&nbsp;移除
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="row">
            <div class="pull-left p-xs">
                <div class="fileinput fileinput-new">
                    <span class="btn btn-primary btn-sm btn-file"><span class="fileinput-new">
                        <i class="fa fa-files-o"></i>&nbsp;选择文件</span>
                        <input type="file" nv-file-select uploader="uploader" multiple="multiple"
                               accept=".csv, application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
                    </span>
                </div>
            </div>
            <div class="pull-right p-xs">
                <button type="button" class="btn btn-danger btn-sm" ng-click="uploader.clearQueue()"
                        ng-disabled="!uploader.queue.length">
                    <i class="fa fa-trash"></i>&nbsp;全部移除
                </button>
            </div>
            <div class="pull-right p-xs">
                <button type="button" class="btn btn-warning btn-sm" ng-click="uploader.cancelAll()"
                        ng-disabled="!uploader.isUploading">
                    <i class="fa fa-ban"></i>&nbsp;全部取消
                </button>
            </div>
            <div class="pull-right p-xs">
                <button type="button" class="btn btn-success btn-sm" ng-click="uploader.uploadAll()"
                        ng-disabled="!uploader.getNotUploadedItems().length">
                    <i class="fa fa-upload"></i>&nbsp;上传全部
                </button>
            </div>
        </div>
    </div>
</div>
<div class="modal-footer">
    <button class="btn btn-warning btn-sm" type="button" ng-click="cancel()">关闭</button>
</div>