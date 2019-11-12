import {MenuComponent} from './components/menuComponent.js';
import {UploadParcelComponent} from './components/uploadParcelComponent.js';
import {StartTestComponent} from './components/startTestComponent.js';
import {ClusterComponent} from './components/clusterComponent.js';
import {TestsComponent} from './components/testsComponent.js';
import {TestExecutionsComponent} from './components/testsExecutionsComponent.js';
import {TestExecutionComponent} from './components/testExecutionComponent.js';
import {TestsService} from './services/testsService.js';
import {ClusterService} from './services/clusterService.js';
import {ExecutionService} from './services/executionService.js';

export const CycloneModule = angular.module('cyclone', [
    'ngRoute',
    'ngFileUpload',
    'ngSanitize',
    'ngToast',
    'ui.bootstrap'
])
    .component('menu', MenuComponent)
    .component('uploadParcel', UploadParcelComponent)
    .component('startTest', StartTestComponent)
    .component('cluster', ClusterComponent)
    .component('tests', TestsComponent)
    .component('testExecutions', TestExecutionsComponent)
    .component('testExecution', TestExecutionComponent)
    .service('testService', TestsService)
    .service('clusterService', ClusterService)
    .service('executionService', ExecutionService)
    .config($routeProvider => {
        $routeProvider.when('/tests/start', {
            template: '<start-test></start-test>'
        }).when('/tests', {
            template: '<tests></tests>'
        }).when('/tests/executions', {
            template: '<test-executions></test-executions>'
        }).when('/tests/executions/:id', {
            template: '<test-execution></test-execution>'
        }).when('/cluster', {
            template: '<cluster></cluster>'
        }).otherwise('/tests/start');
    });