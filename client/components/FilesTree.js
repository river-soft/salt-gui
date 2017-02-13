import React, {PropTypes, Component} from 'react';
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
            addScript: false
        };
        this.showContent = this.showContent.bind(this);
        this.addScript = this.addScript.bind(this);
        this.onRequestClose = this.onRequestClose.bind(this);
        this.editScript = this.editScript.bind(this);
        this.removeScriptState = this.removeScriptState.bind(this);
        this.hideContent = this.hideContent.bind(this);
    }

    componentDidMount() {
        this.props.filesRequest();
    }

    componentDidUpdate() {
        if (this.state.getFiles) {
            this.props.filesRequest();
            this.setState({getFiles: false});
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
            addScript: false
        })
    }

    addScript() {
        this.setState({
            removeScript: false,
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
            editScript: false
        });
    }

    cancelAddGroupAndScript() {
        this.setState({addScript: false});
    }

    cancelEditScript() {
        this.setState({
            editScript: false,
            getFiles: true
        });
    }

    render() {

        let _this = this, template, modal, createEditGroup, fileDescription;

        if (_this.state.showFileDescription) {

            fileDescription = <FileDescription scriptContent={_this.props.scriptContent} editScript={_this.editScript}
                                               removeScript={_this.removeScriptState} script={_this.props.script}/>;
        }

        if (_this.props.files.length === 0) {
            template = <div>Данных нету</div>
        } else if (_this.state.rerender) {
            template = <TreeView groups={_this.state.filterScripts} showContent={this.showContent}/>;
        } else {
            template = <TreeView groups={_this.props.files} showContent={this.showContent}/>;
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
        }

        return <Container>
            <Row>
                <Col md='3' xs='6' lg='3'>
                    <Input label='Поиск' floatingLabel={true} onChange={this.filterTree.bind(this)}/>
                    <ul className='list mui-list--unstyled'>
                        {template}
                    </ul>
                    <button className='mui-btn button' onClick={this.addScript}>добавить</button>
                </Col>
                <Col md='9' xs='6' lg='9'>
                    {this.state.addScript || this.state.editScript ? createEditGroup : null}
                    {this.state.showFileDescription ? fileDescription : null}
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

FilesTree.propTypes = {
    filesRequest: PropTypes.func.isRequired,
    // error: PropTypes.string.isRequired
};