import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {FilesTree} from '../components/FilesTree';
import {Header} from '../components/Header';
import * as filesTreeActions from '../actions/FilesTreeActions';
import * as createGroupActions from '../actions/GroupCreateActions';
import * as getScriptContent from '../actions/GetScriptContentAction';

class App extends Component {

    constructor(props) {
        super(props);

        this.state = {
            createSuccess: false
        };
    }

    componentDidUpdate() {
        if(this.props.createGroup.group) {
            this.setState({createSuccess: true});
            delete this.props.createGroup.group;
        } else if (this.state.createSuccess) {
            this.setState({createSuccess: false});
        }
    }

    render() {

        const _this = this,
            {filesRequest} = _this.props.filesTreeActions,
            {createGroup} = _this.props.createGroupActions,
            {getScriptContent} = _this.props.getScriptContent;

        if (_this.props.createGroup.group) {

            let i = -1;
            _this.props.filesTree.files.filter((item, index) => {
                if (item.group.toLowerCase().search(_this.props.createGroup.group.group.toLowerCase()) !== -1) {
                    i = index;
                }
            });

            if (i >= 0) {
                _this.props.filesTree.files[i].scripts = _this.props.createGroup.group.scripts;
            } else {
                _this.props.filesTree.files.push(_this.props.createGroup.group);
            }
        }

        let filesTree = <FilesTree createGroup={createGroup} filesRequest={filesRequest}
                                   scriptContent={_this.props.scriptContent}
                                   getScriptContent={getScriptContent} files={_this.props.filesTree.files}
                                   error={_this.props.createGroup.error} createSuccess={this.state.createSuccess}/>;

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
        scriptContent: state.getScriptContent
    }
}

function mapDispatchToProps(dispatch) {
    return {
        filesTreeActions: bindActionCreators(filesTreeActions, dispatch),
        createGroupActions: bindActionCreators(createGroupActions, dispatch),
        getScriptContent: bindActionCreators(getScriptContent, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(App)