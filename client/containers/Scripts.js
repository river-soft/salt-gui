import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {ScriptsTree} from '../components/scripts/ScriptsTree';
import {Header} from '../components/Header';
import * as filesTreeActions from '../actions/FilesTreeActions';
import * as createGroupActions from '../actions/GroupCreateActions';
import * as getScriptContent from '../actions/GetScriptContentAction';
import * as scriptRemoveAction from '../actions/ScriptRemoveAction';
import * as editScriptAction from '../actions/EditScriptAction';
import * as editGroupAction from '../actions/GroupEditAction';
import * as removeGroupAction from '../actions/GroupRemoveAction';
import * as getGroupedMinionsAction from '../actions/GetGroupedMinionsAction';
import * as executeScriptsAction from '../actions/ExecuteScriptsAction';
import * as getMessagesAction from '../actions/GetMessagesAction';
import {changeLanguage} from '../helpers';

class Scripts extends Component {

    constructor(props) {
        super(props);

        this.state = {
            createSuccess: false,
            removeSuccess: false,
            editSuccess: false,
            editGroupSuccess: false,
            removeGroupSuccess: false,
            strings: ''
        };
    }

    componentWillMount() {
        if (!this.props.localization) {
            const {getMessages} = this.props.getMessagesAction;

            getMessages();
        } else {
            this.setState({strings: this.props.localization.messages});
        }
    }

    componentDidUpdate() {
        const {filesRequest} = this.props.filesTreeActions;

        if (this.props.createGroup.group) {
            this.setState({createSuccess: true});
            delete this.props.createGroup.group;
        } else if (this.state.createSuccess) {
            this.setState({createSuccess: false});
        }

        if (this.props.editScript.edit) {
            this.setState({editSuccess: true});
            delete this.props.editScript.edit;
        } else if (this.state.editSuccess) {
            this.setState({editSuccess: false});
        }

        if (this.props.scriptRemove.removed) {
            this.setState({removeSuccess: true});
            delete this.props.scriptRemove.removed;
        } else if (this.state.removeSuccess) {
            this.setState({removeSuccess: false});
        }

        if (this.props.editGroup.edit) {
            this.setState({editGroupSuccess: true});
            this.props.editGroup.edit = false;
        } else if (this.state.editGroupSuccess) {
            this.setState({editGroupSuccess: false});
            filesRequest();
        }

        if (this.props.removeGroup.removed) {
            this.setState({removeGroupSuccess: true});
            this.props.removeGroup.removed = false;
        } else if (this.state.removeGroupSuccess) {
            this.setState({removeGroupSuccess: false});
            filesRequest();
        }
    }

    setLanguage(locale) {
        changeLanguage(locale);
        this.state.strings.setLanguage(locale);
        this.setState({changeLocale: true});
    }

    setExecuteFalse() {
        this.props.executeScripts.execute = false;
    }

    setAddGroupAndScriptErrorFalse() {
        this.props.createGroup.error = '';
        this.setState({run: true});
    }

    setEditScriptFalse() {
        this.props.editScript.error = '';
    }

    setRemoveScriptErrorFalse() {
        this.props.scriptRemove.error = '';
    }

    setExecuteErrorFalse() {
        if (this.props.executeScripts.error) {
            this.props.executeScripts.error = '';
            this.setState({go: true});
        }
    }

    render() {

        const _this = this,
            {filesRequest} = _this.props.filesTreeActions,
            {createGroup} = _this.props.createGroupActions,
            {getScriptContent} = _this.props.getScriptContent,
            {scriptRemove} = _this.props.scriptRemoveAction,
            {editScript} = _this.props.editScriptAction,
            {editGroup} = _this.props.editGroupAction,
            {removeGroup} = _this.props.removeGroupAction,
            {getGroupedMinions} = _this.props.getGroupedMinionsAction,
            {executeScripts} = _this.props.executeScriptsAction;

        let executeError = _this.props.executeScripts.error,
            editScriptError = _this.props.editScript.error,
            removeScriptError = _this.props.scriptRemove.error,
            messages = this.state.strings;

        if (_this.props.createGroup.group) {

            let position = -1;
            for (let i = 0; i < _this.props.filesTree.files.length; i++) {

                if (_this.props.filesTree.files[i].group === _this.props.createGroup.group.group) {
                    position = i;
                }
            }


            if (position >= 0) {
                _this.props.filesTree.files[position].scripts = _this.props.createGroup.group.scripts;
            } else {
                _this.props.filesTree.files.push(_this.props.createGroup.group);
            }
        }

        let scriptsTree = <ScriptsTree createGroup={createGroup} filesRequest={filesRequest} executeError={executeError}
                                       removeSuccess={_this.state.removeSuccess} removeScriptError={removeScriptError}
                                       setRemoveScriptErrorFalse={::_this.setRemoveScriptErrorFalse}
                                       scriptContent={_this.props.scriptContent} scriptRemove={scriptRemove}
                                       getScriptContent={getScriptContent} files={_this.props.filesTree.files}
                                       error={_this.props.createGroup.error} createSuccess={_this.state.createSuccess}
                                       editScript={editScript} editSuccess={_this.state.editSuccess}
                                       editScriptError={editScriptError} setEditScriptFalse={::_this.setEditScriptFalse}
                                       editGroup={editGroup} messages={messages}
                                       editGroupSuccess={_this.state.editGroupSuccess}
                                       removeGroup={removeGroup} setExecuteErrorFalse={::_this.setExecuteErrorFalse}
                                       removeGroupSuccess={_this.state.removeGroupSuccess}
                                       getGroupedMinions={getGroupedMinions}
                                       groupedMinions={_this.props.groupedMinions.groupedMinions}
                                       executeScripts={executeScripts}
                                       execute={_this.props.executeScripts.execute}
                                       setExecuteFalse={::_this.setExecuteFalse}
                                       setAddGroupAndScriptErrorFalse={::this.setAddGroupAndScriptErrorFalse}
                                       createdGroup={_this.props.createGroup.group}/>;

        return (<div className='wrapper'>
            <Header header={messages['client.header.scripts.title']} messages={messages}
                    setLanguage={::this.setLanguage}/>
            <main className='main'>
                {scriptsTree}
            </main>
        </div>)
    }
}

function mapStateToProps(state) {
    return {
        filesTree: state.filesTree,
        createGroup: state.createGroup,
        scriptContent: state.getScriptContent,
        scriptRemove: state.scriptRemove,
        editScript: state.editScript,
        editGroup: state.editGroup,
        removeGroup: state.removeGroup,
        groupedMinions: state.groupedMinions,
        executeScripts: state.executeScripts,
        localization: state.localization,
    }
}

function mapDispatchToProps(dispatch) {
    return {
        filesTreeActions: bindActionCreators(filesTreeActions, dispatch),
        createGroupActions: bindActionCreators(createGroupActions, dispatch),
        getScriptContent: bindActionCreators(getScriptContent, dispatch),
        scriptRemoveAction: bindActionCreators(scriptRemoveAction, dispatch),
        editScriptAction: bindActionCreators(editScriptAction, dispatch),
        editGroupAction: bindActionCreators(editGroupAction, dispatch),
        removeGroupAction: bindActionCreators(removeGroupAction, dispatch),
        getGroupedMinionsAction: bindActionCreators(getGroupedMinionsAction, dispatch),
        executeScriptsAction: bindActionCreators(executeScriptsAction, dispatch),
        getMessagesAction: bindActionCreators(getMessagesAction, dispatch),

    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Scripts)