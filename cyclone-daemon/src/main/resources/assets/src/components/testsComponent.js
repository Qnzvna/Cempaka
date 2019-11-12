export const TestsComponent = {
    templateUrl: 'templates/tests.html',
    controller: class TestsController
    {
        constructor(testService)
        {
            this.testService = testService;
        }

        $onInit()
        {
            this.tests = undefined;
            this.loadTests();
        }

        loadTests()
        {
            this.testService.getTests().then(tests => this.tests = this.groupTests(tests));
        }

        groupTests(tests)
        {
            return tests.reduce((map, test) => {
                if (map[test.parcelId] !== undefined) {
                    map[test.parcelId].push(test);
                } else {
                    map[test.parcelId] = [test];
                }
                return map;
            }, {});
        }

        deleteParcel(id)
        {
            this.testService.deleteTest(id).then(() => this.tests = _.omit(this.tests, [id]));
        }
    }
};