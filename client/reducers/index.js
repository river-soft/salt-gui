import {combineReducers} from 'redux';
import filesTree from './filesTree';
import createGroup from './createGroup';
import getScriptContent from './getScriptContent';
import scriptRemove from './scriptRemove';
import editScript from './editScript';
import minions from './minions';

export default combineReducers({
    getScriptContent: getScriptContent,
    filesTree: filesTree,
    createGroup: createGroup,
    scriptRemove: scriptRemove,
    editScript: editScript,
    minions: minions
})