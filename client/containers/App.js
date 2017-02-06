import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {FilesTree} from '../components/FilesTree';
import {Header} from '../components/Header';
import * as filesTreeActions from '../actions/FilesTreeActions';
import * as createGroupActions from '../actions/GroupCreateActions';

class App extends Component {

    render() {

        const _this = this;
        const {filesRequest} = _this.props.filesTreeActions;
        const {createGroup} = _this.props.createGroupActions;

        if (_this.props.createGroup.group) {

            let i;
            _this.props.filesTree.files.filter((item, index) => {
                if(item.group.toLowerCase().search(_this.props.createGroup.group.group.toLowerCase()) !== -1) {
                    i = index;
                }
            });

            if (i) {
                _this.props.filesTree.files[i].scripts = _this.props.createGroup.group.scripts;
            } else {
                _this.props.filesTree.files.push(_this.props.createGroup.group);
            }
        }

        let filesTree = <FilesTree createGroup={createGroup} filesRequest={filesRequest}
                                   files={_this.props.filesTree.files}/>;

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