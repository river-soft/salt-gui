import {combineReducers} from 'redux';
import filesTree from './filesTree';
import createGroup from './createGroup';
import getScriptContent from './getScriptContent';
import scriptRemove from './scriptRemove';

export default combineReducers({
    getScriptContent: getScriptContent,
    filesTree: filesTree,
    createGroup: createGroup,
    scriptRemove: scriptRemove
})