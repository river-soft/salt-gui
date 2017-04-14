import React, {Component} from 'react';
import ScriptDetails from './ScriptDetails';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import Container from 'muicss/lib/react/container';
import TreeView from '../tree/TreeView';
import Input from 'muicss/lib/react/input';
import CreateScriptGroup from './CreateScriptGroup';
import Modal from 'react-modal';
import EditScript from './EditScript';
import RemoveScript from './RemoveScript';
import EditMinionsGroup from '../minions/EditMinionsGroupModal';
import RemoveMinionsGroupModal from '../minions/RemoveMinionsGroupModal';
import TreeViewModalCheckboxes from '../treeModalCheckboxes/TreeViewModalCheckboxes';
import {containsRole} from '../../helpers';
import cookie from 'react-cookie';

export class ScriptsTree extends Component {

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
            this.setState({
                showModal: false,
                removeGroup: false,
            });
        }

        if (this.props.execute && this.state.runScript) {
            this.setState({
                runScript: false
            });
            this.props.setExecuteFalse();
        }
    }

    hideContent() {
        this.setState({
            showFileDescription: false,
            getFiles: true
        });
    }

    showContent(scriptId, user) {

        if (containsRole(user.roles, ['ROLE_SHOW_SCRIPT_DETAILS', 'ROLE_ROOT'])) {
            this.props.getScriptContent(scriptId);
            this.props.setEditScriptFalse();
            this.props.setRemoveScriptErrorFalse();
            this.setState({
                showFileDescription: true,
                addScript: false,
                editScript: false,
                runScript: false
            })
        }
    }

    addScript() {
        this.props.setAddGroupAndScriptErrorFalse();

        this.setState({
            addScript: true,
            editScript: false,
            removeScript: false,
            showFileDescription: false
        });
    }

    filterTree(e) {
        let obj = [];

        if (e.target.value) {
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
        } else {
            this.setState({
                filterScripts: [],
                rerender: false
            })
        }
    }

    onRequestClose() {
        this.setState({showModal: false});
    }

    editScript(script) {
        this.props.setEditScriptFalse();
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
            editGroup: false,
            editScript: false,
            runScript: false,
            removeGroup: false
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

        this.props.setEditScriptFalse();
        this.props.getScriptContent(this.state.editingScript.id);
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
        this.props.setExecuteFalse();
        this.props.setExecuteErrorFalse();
    }

    render() {

        let token = cookie.load('accessToken'),
            user = token ? JSON.parse(atob(token)) : null;

        if (user && typeof user.roles === 'string') {
            user.roles = user.roles.replace(/[\[\]]/g, '').split(',');
        }

        let _this = this, template, modal, createEditGroup, fileDescription, selectMinions, messages = this.props.messages;

        let permittedRoles = {
            edit: ['ROLE_EDIT_SCRIPTS_GROUP', 'ROLE_ROOT'],
            delete: ['ROLE_DELETE_SCRIPTS_GROUP', 'ROLE_ROOT'],
            create: ['ROLE_CREATE_SCRIPT_AND_GROUP', 'ROLE_ROOT']
        };

        if (_this.state.showFileDescription) {

            fileDescription = <ScriptDetails scriptContent={_this.props.scriptContent} editScript={::_this.editScript}
                                             removeScript={::_this.removeScriptState} script={_this.props.script}
                                             runScript={::_this.runScript} messages={messages}
                                             user={user}/>;
        }

        if (_this.props.files.length === 0) {
            template = <div>{messages['client.messages.no.data']}</div>
        } else if (_this.state.rerender) {
            template =
                <TreeView groups={_this.state.filterScripts} showContent={::_this.showContent} removeIfNotEmpty={false}
                          removeGroup={::_this.removeGroup} rerender={true} editGroup={::_this.editGroup}
                          createdGroup={_this.props.createdGroup} messages={messages}
                          permittedRoles={permittedRoles}
                          user={user}/>;
        } else {
            template =
                <TreeView groups={_this.props.files} showContent={::_this.showContent} editGroup={::_this.editGroup}
                          removeIfNotEmpty={false} removeGroup={::_this.removeGroup}
                          rerender={false} messages={messages}
                          createdGroup={_this.props.createdGroup}
                          permittedRoles={permittedRoles}
                          user={user}/>;
        }

        if (_this.state.editScript) {
            createEditGroup = <EditScript closeModal={::_this.onRequestClose} script={_this.state.editingScript}
                                          cancel={::_this.cancelEditScript} groups={_this.props.files}
                                          editScript={_this.props.editScript} editSuccess={_this.props.editSuccess}
                                          editScriptError={_this.props.editScriptError} messages={messages}/>
        } else if (_this.state.removeScript) {
            modal = <RemoveScript closeModal={::_this.onRequestClose} scriptRemove={_this.props.scriptRemove}
                                  filesRequest={_this.props.filesRequest}
                                  removeScriptError={_this.props.removeScriptError}
                                  script={_this.state.editingScript} removeSuccess={_this.props.removeSuccess}
                                  hideContent={::_this.hideContent} messages={messages}/>
        } else if (this.state.addScript) {
            createEditGroup = <CreateScriptGroup createGroup={_this.props.createGroup} groups={_this.props.files}
                                                 error={_this.props.error} messages={messages}
                                                 createSuccess={_this.props.createSuccess}
                                                 cancel={::_this.cancelAddGroupAndScript}/>
        } else if (_this.state.runScript) {
            selectMinions =
                <TreeViewModalCheckboxes groups={_this.props.groupedMinions}
                                         scriptName={_this.state.scriptName}
                                         executeScripts={_this.props.executeScripts}
                                         minions={true} messages={messages}
                                         execute={_this.props.execute} setExecuteErrorFalse={_this.props.setExecuteErrorFalse}
                                         executeError={_this.props.executeError}/>
        }

        if (_this.state.showModal) {
            if (_this.state.removeGroup) {
                modal = <RemoveMinionsGroupModal group={_this.state.removedGroup} closeModal={::_this.onRequestClose}
                                                 removeGroup={_this.props.removeGroup} messages={messages}/>
            } else if (_this.state.editGroup) {
                modal = <EditMinionsGroup group={_this.state.editedGroup} closeModal={::_this.onRequestClose}
                                          groups={_this.props.files} messages={messages}
                                          edit={_this.props.editGroup}/>
            }
        }

        return <Container>
            <Row>
                <Col md='3' xs='6' lg='3'>

                    {containsRole(user.roles, ['ROLE_SHOW_GROUPED_SCRIPTS', 'ROLE_ROOT']) ?
                        <div>
                            <Input label={messages['client.input.search.scripts']} id='filter-tree' floatingLabel={true}
                                   onChange={::_this.filterTree}/>
                            <ul className='list mui-list--unstyled'>
                                {template}
                            </ul>
                        </div> : null}

                    {containsRole(user.roles, permittedRoles.create) ?

                        <button className='mui-btn button' onClick={::_this.addScript}
                                title={messages['client.scripts.btn.add.scripts.title']}>
                            {messages['client.btn.add']}
                        </button>
                        : null
                    }

                </Col>
                <Col md='9' xs='6' lg='9'>
                    {_this.state.addScript || _this.state.editScript ? createEditGroup : null}
                    {_this.state.showFileDescription ? fileDescription : null}
                    {_this.state.runScript ? selectMinions : null}
                    {_this.props.execute ?
                        <span className='success-mess'>{messages['client.message.script.run.success']}</span> : null}
                </Col>
            </Row>
            <Modal contentLabel='label' isOpen={_this.state.showModal} className='modal'
                   onRequestClose={::_this.onRequestClose} overlayClassName='overlay'
                   parentSelector={() => document.body} ariaHideApp={false}>
                {modal}
            </Modal>
        </Container>
    }
}