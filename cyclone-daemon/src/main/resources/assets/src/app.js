import {MenuComponent} from './components/menu.component.js';
import {ParcelComponent} from './components/parcel.component.js';
import {RunTestComponent} from './components/run.test.component.js';
import {TestComponent} from './components/test.component.js';
import {StatusComponent} from './components/status.component.js';
import {TestsHistoryComponent} from './components/testsHistoryComponent.js';

export const CycloneModule = angular.module('cyclone', [
    'ngRoute',
    'ngFileUpload',
    'ngSanitize',
    'ngToast'
])
    .component('menu', MenuComponent)
    .component('parcel', ParcelComponent)
    .component('runTest', RunTestComponent)
    .component('test', TestComponent)
    .component('status', StatusComponent)
    .component('testsHistory', TestsHistoryComponent)
    .config($routeProvider => {
        $routeProvider.when('/parcels/add', {
            template: '<parcel></parcel>'
        }).when('/tests/parcel/:parcelId/name/:testName', {
            template: '<test></test>'
        }).when('/tests/run', {
            template: '<run-test></run-test>'
        }).when('/tests/history', {
            template: '<tests-history></tests-history>'
        }).when('/status', {
            template: '<status></status>'
        }).otherwise('/status');
    });