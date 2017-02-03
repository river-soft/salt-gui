import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {FilesTree} from '../components/FilesTree';
import {Header} from '../components/Header';
import * as filesTreeActions from '../actions/FilesTreeActions';
import * as createGroupActions from '../actions/GroupCreateActions';

class App extends Component {

    render() {

        const {filesRequest} = this.props.filesTreeActions;
        const {createGroup} = this.props.createGroupActions;
        let filesTree = <FilesTree createGroup={createGroup} filesRequest={filesRequest} files={this.props.filesTree.files}/>;

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
        createGroup: state.createGroup
    }
}

function mapDispatchToProps(dispatch) {
    return {
        filesTreeActions: bindActionCreators(filesTreeActions, dispatch),
        createGroupActions: bindActionCreators(createGroupActions, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(App)