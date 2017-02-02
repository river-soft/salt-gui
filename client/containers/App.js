import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {FilesTree} from '../components/FilesTree';
import {Header} from '../components/Header';
import * as filesTreeActions from '../actions/FilesTreeActions';

class App extends Component {

    render() {

        const {filesRequest} = this.props.filesTreeActions;
        let filesTree = <FilesTree filesRequest={filesRequest} files={this.props.filesTree.files}/>;

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
        filesTree: state.filesTree
    }
}

function mapDispatchToProps(dispatch) {
    return {
        filesTreeActions: bindActionCreators(filesTreeActions, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(App)