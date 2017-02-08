import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {FilesTree} from '../components/FilesTree';
import {Header} from '../components/Header';
import * as filesTreeActions from '../actions/FilesTreeActions';
import * as createGroupActions from '../actions/GroupCreateActions';
import * as getScriptContent from '../actions/GetScriptContentAction';
import * as scriptRemoveAction from '../actions/ScriptRemoveAction';
import * as editScriptAction from '../actions/EditScriptAction';

class App extends Component {

    constructor(props) {
        super(props);

        this.state = {
            createSuccess: false,
            removeSuccess: false,
            editSuccess: false
        };
    }

    componentDidUpdate() {
        if (this.props.createGroup.group) {
            this.setState({createSuccess: true});
            delete this.props.createGroup.group;
        } else if (this.state.createSuccess) {
            this.setState({createSuccess: false});
        }

        if(this.props.editScript.edit) {
            this.setState({editSuccess: true});
            delete this.props.editScript.edit;
        } else if(this.state.editSuccess) {
            this.setState({editSuccess: false});
        }

        if(this.props.scriptRemove.removed) {
            this.setState({removeSuccess: true});
            delete this.props.scriptRemove.removed;
        } else if(this.state.removeSuccess) {
            this.setState({removeSuccess: false});
        }
    }

    render() {

        const _this = this,
            {filesRequest} = _this.props.filesTreeActions,
            {createGroup} = _this.props.createGroupActions,
            {getScriptContent} = _this.props.getScriptContent,
            {scriptRemove} = _this.props.scriptRemoveAction,
            {editScript} = _this.props.editScriptAction;

        if (_this.props.createGroup.group) {

            let position = -1;
            for(let i = 0; i < _this.props.filesTree.files.length; i++) {

                if(_this.props.filesTree.files[i].group === _this.props.createGroup.group.group) {
                    position = i;
                }
            }


            if (position >= 0) {
                _this.props.filesTree.files[position].scripts = _this.props.createGroup.group.scripts;
            } else {
                _this.props.filesTree.files.push(_this.props.createGroup.group);
            }
        }

        let filesTree = <FilesTree createGroup={createGroup} filesRequest={filesRequest}
                                   removeSuccess={this.state.removeSuccess}
                                   scriptContent={_this.props.scriptContent} scriptRemove={scriptRemove}
                                   getScriptContent={getScriptContent} files={_this.props.filesTree.files}
                                   error={_this.props.createGroup.error} createSuccess={this.state.createSuccess}
                                   editScript={editScript} editSuccess={this.state.editSuccess}/>;

        return (<div className='wrapper'>
            <Header />
            <main className='main'>
                {filesTree}
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
        editScript: state.editScript
    }
}

function mapDispatchToProps(dispatch) {
    return {
        filesTreeActions: bindActionCreators(filesTreeActions, dispatch),
        createGroupActions: bindActionCreators(createGroupActions, dispatch),
        getScriptContent: bindActionCreators(getScriptContent, dispatch),
        scriptRemoveAction: bindActionCreators(scriptRemoveAction, dispatch),
        editScriptAction: bindActionCreators(editScriptAction, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(App)