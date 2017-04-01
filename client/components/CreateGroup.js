import React, {Component} from 'react';
import Form from 'muicss/lib/react/form';
import Input from 'muicss/lib/react/input';
import Button from 'muicss/lib/react/button';
import Divider from 'muicss/lib/react/divider';
import AceEditor from 'react-ace';

import 'brace/mode/yaml';
import 'brace/theme/eclipse';

export default class CreateGroup extends Component {

    constructor(props) {
        super(props);

        this.state = {
            addScript: false,
            groupName: '',
            scriptName: '',
            scriptContent: '',
            scripts: [],
            showDropdown: false,
            rerenderList: [],
            rerenderDropdown: false,
            scriptExist: false,
            closeModal: false
        };

        this.addScript = this.addScript.bind(this);
        this.createModel = this.createModel.bind(this);
    }

    componentDidUpdate() {
        if (this.props.createSuccess) {
            this.setState({
                addScript: false,
                groupName: '',
                scriptName: '',
                scriptContent: '',
                scripts: [],
                showDropdown: false,
                rerenderList: [],
                rerenderDropdown: false,
                editingScript: '',
                editingScriptPosition: 0
            });

            this.props.cancel();
        }
    }

    addScript() {
        this.setState({
            addScript: true
        });
    }

    editScript(scriptName, scriptContent) {

        for(let i = 0; i < this.state.scripts.length; i++) {
            if (this.state.scripts[i].name === scriptName) {
                this.state.editingScript = this.state.scripts[i];
                this.state.editingScriptPosition = i;
                this.state.scripts.splice(i, 1);
            }
        }

        this.setState({
            addScript: true,
            scriptName: scriptName,
            scriptContent: scriptContent
        })
    }

    setScriptName(name) {
        this.setState({scriptName: name});
    }

    setScriptContent(content) {
        this.setState({scriptContent: content});
    }

    setGroupName(name) {
        let input = document.getElementById('group'),
            _this = this;

        document.onclick = function (e) {
            if (e.target.className != input.className && _this.state.showDropdown) {
                if(e.target.className != 'dropdown-btn') {
                    _this.setState({showDropdown: false});
                }
            }
        };

        this.setState({
            groupName: name,
            showDropdown: true
        });
    }

    showDropdown() {

        let input = document.getElementById('group'),
            _this = this;

        document.onclick = function (e) {
            if (e.target.className != input.className && _this.state.showDropdown) {
                if(e.target.className != 'dropdown-btn') {
                    _this.setState({showDropdown: false});
                }
            }
        };

        this.setState({showDropdown: true});
    }

    cancelAddScript() {

        if(this.state.editingScript) {

            this.state.scripts.splice(this.state.editingScriptPosition, 0, this.state.editingScript);
        }

        this.setState({
            addScript: false,
            scriptName: '',
            scriptContent: '',
            scriptExist: false,
            editingScript: '',
            editingScriptPosition: ''
        });
    }

    validateScript(scriptName) {

        const groups = this.props.groups;
        let exist = false;
        let i;
        for (i = 0; i < groups.length; i++) {
            groups[i].scripts.filter((script) => {
                if (script.name.toLowerCase() === scriptName.toLowerCase()) {
                    exist = true;
                }
            })
        }

        for (i = 0; i < this.state.scripts.length; i++) {
            if (this.state.scripts[i].name.toLowerCase() === scriptName.toLowerCase()) {
                exist = true;
            }
        }

        this.setState({
            scriptExist: exist
        });
    }

    selectGroup(group) {
        let input = document.getElementById('group');

        input.value = group;
        input.focus();
        input.className.replace('mui--is-empty', '');
        input.className += ' mui--is-not-empty';
        this.setState({showDropdown: false});
    }

    rerenderGroupList(groupName) {
        let obj = [];

        let group = this.props.groups.filter((item) => {
            return item.group.toLowerCase().search(groupName.toLowerCase()) !== -1
        });

        for (let i = 0; i < group.length; i++) {
            obj.push({
                group: group[i].group,
            })
        }

        this.setState({
            rerenderList: obj,
            rerenderDropdown: true
        });
    }

    addScriptForm() {

        return <div><Input label='Название скрипта' floatingLabel={true} onChange={(e) => {
            this.setScriptName(e.target.value);
            this.validateScript(e.target.value);
        }} defaultValue={this.state.scriptName || ''}/>
            {this.state.scriptExist ? <span className='input_error'>Скрипт с таким именем уже существует</span> : null}

            <AceEditor
                mode='yaml'
                theme='eclipse'
                width='100%'
                onChange={(content) => {
                    this.setScriptContent(content);
                }}
                value={this.state.scriptContent || ''}
            />

            <Button size='small' color='primary' variant='flat'
                    onClick={() => {
                        this.addScript();
                        this.createScriptStore();
                    }} className='modal__btn'
                    disabled={!this.state.scriptName.length || !this.state.scriptContent || this.state.scriptExist}>Добавить</Button>
            <Button size='small' color='primary' variant='flat' onClick={() => {
                this.cancelAddScript();
            }} className='modal__btn'>Отмена</Button>
        </div>
    }

    createScriptStore() {
        this.state.scripts.push({
            name: this.state.scriptName,
            content: this.state.scriptContent
        });

        this.setState({
            addScript: false,
            scriptName: '',
            scriptContent: ''
        });
    }

    createModel() {
        let obj = {
            group: this.state.groupName,
            scripts: []
        };

        this.state.scripts.map((script) => {
            obj.scripts.push({
                name: script.name,
                content: script.content
            })
        });

        this.props.createGroup(obj);
    }

    render() {

        let dropdownList = !this.state.rerenderDropdown ?
            this.props.groups.length > 0 ?
                <ul className='group__list mui-list--unstyled' ref='dropdown'>
                    {this.props.groups.map((group, index) => {
                        return <li className='group__list_item' key={index} onClick={() => {
                            this.selectGroup(group.group)
                        }}>{group.group}</li>
                    })}
                </ul> : null :
            this.state.rerenderList.length > 0 ?
                <ul className='group__list mui-list--unstyled' ref='dropdown'>
                    {this.state.rerenderList.map((group, index) => {
                        return <li className='group__list_item' key={index} onClick={() => {
                            this.selectGroup(group.group);
                        }}>{group.group}</li>
                    })}
                </ul> : null;

        return <div className='modal__content'>
            <h4 className='mui--text-center modal__header'>Создание группы и скриптов</h4>
            <Form className='modal__form'>
                <div className='modal__form_group'>
                    <Input label='Название группы' floatingLabel={true} name='group' id='group' onChange={(e) => {
                        this.setGroupName(e.target.value);
                        this.rerenderGroupList(e.target.value);
                    }} onFocus={(e) => {
                        this.setGroupName(e.target.value)
                    }}/>
                    {this.props.error ? <span className='input_error'>{this.props.error.message}</span> : null}
                    <div className='dropdown-btn' onClick={::this.showDropdown}><span
                        className='mui-caret'></span></div>
                    {this.state.showDropdown ? dropdownList : null}
                </div>
                <div className='modal__content_scripts'>
                    <ul className='mui-list--inline scripts__list'>
                        {this.state.scripts.length > 0 ? this.state.scripts.map((script, index) => {
                                return <li key={index} className='scripts__list_item'
                                           onClick={() => {
                                               this.editScript(script.name, script.content);
                                           }}>{script.name}</li>
                            }) : null}
                    </ul>
                    {this.state.addScript ? null :
                        <Button size='small' color='primary' variant='flat' onClick={::this.addScript}
                                className='modal__btn mui--pull-right'>
                            Добавить скрипт
                        </Button>}
                </div>
                {this.state.addScript ? this.addScriptForm() : null}
            </Form>
            {!this.state.addScript ?
                <div>
                    <Divider />
                    <Button size='small' color='primary' variant='flat'
                            onClick={this.props.cancel} className='modal__btn mui--pull-right'>Отменить</Button>
                    <Button size='small' color='primary' variant='flat'
                            onClick={this.createModel} className='modal__btn mui--pull-right'
                            disabled={!this.state.groupName}>Сохранить</Button>
                </div> : null}
        </div>
    }
}