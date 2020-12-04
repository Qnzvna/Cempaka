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
import {MetadataService} from './services/metadataService.js';
import {MetadataListComponent} from './components/metadataListComponent.js';
import {MetadataComponent} from './components/metadataComponent.js';
import {MetadataForm} from './components/metadataForm.js';

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
    .component('metadataList', MetadataListComponent)
    .component('metadata', MetadataComponent)
    .component('metadataForm', MetadataForm)
    .service('testService', TestsService)
    .service('clusterService', ClusterService)
    .service('executionService', ExecutionService)
    .service('metadataService', MetadataService)
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
        }).when('/metadata', {
            template: '<metadata-list></metadata-list>'
        }).when('/metadata/add', {
            template: '<metadata></metadata>'
        }).otherwise('/tests/start');
    });