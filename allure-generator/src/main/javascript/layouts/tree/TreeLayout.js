import AppLayout from '../application/AppLayout';
import TreeCollection from '../../data/tree/TreeCollection';
import {Model} from 'backbone';
import TestResultTreeView from '../../components/testresult-tree/TestResultTreeView';
import router from '../../router';

export default class TreeLayout extends AppLayout {

    initialize({url}) {
        super.initialize();
        this.tree = new TreeCollection([], {url});
        this.routeState = new Model();
    }

    loadData() {
        return this.tree.fetch();
    }

    getContentView() {
        const {baseUrl, tabName} = this.options;
        return this.createTestResultTreeView(this.tree, this.routeState, tabName, baseUrl);
    }

    createTestResultTreeView(tree, routeState, tabName, baseUrl) {
        return new TestResultTreeView({tree: tree, routeState: routeState, tabName, baseUrl});
    }

    onViewReady() {
        const {testGroup, testResult, testResultTab} = this.options;
        this.onRouteUpdate(testGroup, testResult, testResultTab);
    }

    onRouteUpdate(testGroup, testResult, testResultTab) {
        this.routeState.set('treeNode', {testGroup, testResult});
        this.routeState.set('testResultTab', testResultTab);

        const attachment = router.getUrlParams().attachment;
        if (attachment) {
            this.routeState.set('attachment', attachment);
        } else {
            this.routeState.unset('attachment');
        }

    }
}
