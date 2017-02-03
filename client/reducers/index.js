import {combineReducers} from 'redux';
import filesTree from './filesTree';
import createGroup from './createGroup';

export default combineReducers({
    filesTree: filesTree,
    createGroup: createGroup
})