import React, {Component} from 'react';
import FileDescription from '../components/FileDescription';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import Container from 'muicss/lib/react/container';
import TreeView from './tree/TreeView';
import Input from 'muicss/lib/react/input';
import CreateGroup from './CreateGroup';
import Modal from 'react-modal';
import EditScript from './EditScript';
import RemoveScript from './RemoveScript';
import EditMinionsGroup from './minions/EditMinionsGroupModal';
import RemoveMinionsGroupModal from './minions/RemoveMinionsGroupModal';
import TreeViewModalCheckboxes from './treeModalCheckboxes/TreeViewModalCheckboxes';

export class FilesTree extends Component {

    constructor(props) {
        super(props);

        this.state = {
            showFileDescription: false,
            scriptName: '',
            filterScripts: [],
            rerender: false,
            showModal: false,
            editScript: false,
            removeScript: false,
            getFiles: false,
            addScript: false,
            editGroup: false,
            removeGroup: false,
            runScript: false,
            editedGroup: {},
            removedGroup: {}
        };
        this.showContent = this.showContent.bind(this);
        this.addScript = this.addScript.bind(this);
        this.onRequestClose = this.onRequestClose.bind(this);
        this.editScript = this.editScript.bind(this);
        this.removeScriptState = this.removeScriptState.bind(this);
        this.hideContent = this.hideContent.bind(this);
    }

    componentDidMount() {
        if (typeof this.props.filesRequest === 'function') {
            this.props.filesRequest();
        }
    }

    componentDidUpdate() {
        if (this.state.getFiles) {
            this.props.filesRequest();
            this.setState({getFiles: false});
        }

        if (this.props.editGroupSuccess || this.props.removeGroupSuccess) {
            this.setState({showModal: false});
        }
    }

    hideContent() {
        this.setState({
            showFileDescription: false,
            getFiles: true
        });
    }

    showContent(scriptId) {
        this.props.getScriptContent(scriptId);

        this.setState({
            showFileDescription: true,
            addScript: false,
            editScript: false,
            runScript: false
        })
    }

    addScript() {
        this.setState({
            addScript: true,
            editScript: false,
            showFileDescription: false
        });
    }

    filterTree(e) {
        let obj = [];

        for (let i = 0; i < this.props.files.length; i++) {

            let scripts = this.props.files[i].scripts.filter((item) => {
                return item.name.toLowerCase().search(e.target.value.toLowerCase()) !== -1
            });

            if (scripts.length > 0) {
                obj.push({
                    group: this.props.files[i].group,
                    scripts: scripts
                })
            }
        }

        this.setState({
            filterScripts: obj,
            rerender: true
        });
    }

    onRequestClose() {
        this.setState({showModal: false});
    }

    editScript(script) {
        this.setState({
            removeScript: false,
            editScript: true,
            addScript: false,
            showFileDescription: false,
            editingScript: script
        });
    }

    removeScriptState(script) {
        this.setState({
            removeScript: true,
            editingScript: script,
            showModal: true,
            editScript: false,
            runScript: false

        });
    }

    cancelAddGroupAndScript() {
        this.setState({addScript: false});
    }

    cancelEditScript() {
        this.setState({
            editScript: false,
            getFiles: true,
            showFileDescription: true
        });
    }

    editGroup(groupId, groupName) {
        this.setState({
            editGroup: true,
            removeScript: false,
            removeGroup: false,
            showModal: true,
            editedGroup: {
                id: groupId,
                name: groupName
            }
        });
    }

    removeGroup(groupId, groupName) {
        this.setState({
            removeGroup: true,
            editGroup: false,
            runScript: false,
            showModal: true,
            removedGroup: {
                id: groupId,
                name: groupName
            }
        })
    }

    runScript(scriptName) {
        this.setState({
            runScript: true,
            addScript: false,
            editScript: false,
            showFileDescription: false,
            scriptName: scriptName
        });

        this.props.getGroupedMinions();
    }

    render() {

        let _this = this, template, modal, createEditGroup, fileDescription, selectMinions;

        if (_this.state.showFileDescription) {

            fileDescription = <FileDescription scriptContent={_this.props.scriptContent} editScript={_this.editScript}
                                               removeScript={_this.removeScriptState} script={_this.props.script}
                                               runScript={::this.runScript}/>;
        }

        if (_this.props.files.length === 0) {
            template = <div>Данных нету</div>
        } else if (_this.state.rerender) {
            template =
                <TreeView groups={_this.state.filterScripts} showContent={this.showContent} removeIfNotEmpty={false}
                          removeGroup={::this.removeGroup}/>;
        } else {
            template =
                <TreeView groups={_this.props.files} showContent={this.showContent} editGroup={::this.editGroup}
                          removeIfNotEmpty={false} removeGroup={::this.removeGroup}/>;
        }

        if (_this.state.editScript) {
            createEditGroup = <EditScript closeModal={_this.onRequestClose} script={_this.state.editingScript}
                                          cancel={::this.cancelEditScript} groups={_this.props.files}
                                          editScript={this.props.editScript} editSuccess={this.props.editSuccess}/>
        } else if (_this.state.removeScript) {
            modal = <RemoveScript closeModal={_this.onRequestClose} scriptRemove={_this.props.scriptRemove}
                                  filesRequest={this.props.filesRequest}
                                  script={_this.state.editingScript} removeSuccess={_this.props.removeSuccess}
                                  hideContent={this.hideContent}/>
        } else if (this.state.addScript) {
            createEditGroup = <CreateGroup createGroup={_this.props.createGroup} groups={_this.props.files}
                                           error={_this.props.error}
                                           createSuccess={_this.props.createSuccess}
                                           cancel={::this.cancelAddGroupAndScript}/>
        } else if (_this.state.runScript) {
            selectMinions =
                <TreeViewModalCheckboxes groups={_this.props.groupedMinions}
                                         scriptName={this.state.scriptName}
                                         executeScripts={this.props.executeScripts}
                                         minions={true}/>
        }

        if (_this.state.showModal) {
            if (_this.state.removeGroup) {
                modal = <RemoveMinionsGroupModal group={_this.state.removedGroup} closeModal={_this.onRequestClose}
                                                 removeGroup={_this.props.removeGroup}/>
            } else if (_this.state.editGroup) {
                modal = <EditMinionsGroup group={_this.state.editedGroup} closeModal={_this.onRequestClose}
                                          groups={_this.props.files}
                                          edit={_this.props.editGroup}/>
            }
        }

        return <Container>
            <Row>
                <Col md='3' xs='6' lg='3'>
                    <Input label='Поиск' floatingLabel={true} onChange={::this.filterTree}/>
                    <ul className='list mui-list--unstyled'>
                        {template}
                    </ul>
                    <button className='mui-btn button' onClick={this.addScript}>добавить</button>
                </Col>
                <Col md='9' xs='6' lg='9'>
                    {this.state.addScript || this.state.editScript ? createEditGroup : null}
                    {this.state.showFileDescription ? fileDescription : null}
                    {this.state.runScript ? selectMinions : null}
                </Col>
            </Row>
            <Modal contentLabel='label' isOpen={this.state.showModal} className='modal'
                   onRequestClose={this.onRequestClose.bind(this)} overlayClassName='overlay'
                   parentSelector={() => document.body} ariaHideApp={false}>
                {modal}
            </Modal>
        </Container>
    }
}