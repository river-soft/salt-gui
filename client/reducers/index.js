import {combineReducers} from 'redux';
import filesTree from './filesTree';
import createGroup from './createGroup';
import getScriptContent from './getScriptContent';
import scriptRemove from './scriptRemove';
import editScript from './editScript';
import minions from './minions';
import minionsGroups from './minionsGroups';
import acceptMinions from './acceptMinions';
import rejectMinions from './rejectMinions';
import deleteMinions from './deleteMinions';
import groupedMinions from './groupedMinions';
import minionDetails from './minionDetails';
import createMinionsGroup from './createMinionsGroup';
import removeMinionsGroup from './removeMinionsGroup';
import editMinionsGroup from './editMinionsGroup';
import getGroupsByMinion from './getGroupsByMinion';
import editMinionGroups from './editMinionGroups';
import editGroup from './editGroup';
import removeGroup from './removeGroup';
import executeScripts from './executeScripts';
import jobResults from './jobResults';
import jobDetails from './jobDetails';
import reExecuteScripts from './reExecuteScripts';

export default combineReducers({
    getScriptContent: getScriptContent,
    filesTree: filesTree,
    createGroup: createGroup,
    scriptRemove: scriptRemove,
    editScript: editScript,
    minions: minions,
    minionsGroups: minionsGroups,
    acceptMinions: acceptMinions,
    rejectMinions: rejectMinions,
    deleteMinions: deleteMinions,
    groupedMinions: groupedMinions,
    minionDetails: minionDetails,
    createMinionsGroup: createMinionsGroup,
    removeMinionsGroup: removeMinionsGroup,
    editMinionsGroup: editMinionsGroup,
    getGroupsByMinion: getGroupsByMinion,
    editMinionGroups: editMinionGroups,
    editGroup: editGroup,
    removeGroup: removeGroup,
    executeScripts: executeScripts,
    reExecuteScripts: reExecuteScripts,
    jobResults: jobResults,
    jobDetails: jobDetails
})