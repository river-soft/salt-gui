import React, {Component} from 'react';
import Form from 'muicss/lib/react/form';
import Input from 'muicss/lib/react/input';
import Divider from 'muicss/lib/react/divider';
import Button from 'muicss/lib/react/button';
import AceEditor from 'react-ace';

import 'brace/mode/yaml';
import 'brace/theme/eclipse';


export default class EditScript extends Component {

    constructor(props) {
        super(props);

        this.state = {
            id: '',
            group: '',
            name: '',
            content: '',
            closeModal: false,
            scriptExists: false,
            showDropdown: false,
            rerenderList: [],
            rerenderDropdown: false
        };

        this.editScript = this.editScript.bind(this);
    }

    componentDidMount() {
        this.setState({
            id: this.props.script.id,
            group: this.props.script.group,
            name: this.props.script.name,
            content: this.props.script.content,
        })
    }

    componentDidUpdate() {
        if (this.props.editSuccess) {
            this.setState({
                id: '',
                group: '',
                name: '',
                content: '',
            });

            this.props.cancel();
        }
    }

    setName(name) {
        this.setState({name: name});
    }

    setGroup(group) {
        let input = document.getElementById('group'),
            _this = this;

        document.onclick = function (e) {
            if (e.target.className != input.className && _this.state.showDropdown) {
                _this.setState({showDropdown: false});
            }
        };

        this.setState({
            group: group,
            showDropdown: true
        });
    }

    showDropdown() {
        this.setState({showDropdown: true});
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

    setContent(content) {
        this.setState({content: content})
    }

    editScript() {
        let script = {
            id: this.state.id,
            group: this.state.group,
            name: this.state.name,
            content: this.state.content
        };

        this.props.editScript(script);
    }

    setContentOnLoad() {
        this.setState({content: this.props.script.content})
    }

    validateScript(scriptName) {

        const groups = this.props.groups;
        let exist = false;

        for (let i = 0; i < groups.length; i++) {
            groups[i].scripts.filter((script) => {
                if (script.name.toLowerCase() === scriptName.toLowerCase()) {
                    exist = true;
                }
            })
        }

        this.setState({
            scriptExist: exist
        });
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
            <div className='modal__close_btn' onClick={this.props.closeModal}>X</div>
            <h4 className='mui--text-center modal__header'>Редактирование скрипта</h4>
            <Form className='modal__form'>
                <div className='modal__form_group'>
                    <Input label='Название группы' floatingLabel={true} name='group' id='group'
                           defaultValue={this.props.script.group || ''}
                           onChange={(e) => {
                               this.setGroup(e.target.value);
                               this.rerenderGroupList(e.target.value);
                           }} onFocus={(e) => {
                        this.setGroup(e.target.value);
                    }}/>
                    <div className='dropdown-btn' onClick={this.showDropdown.bind(this)}><span
                        className='mui-caret'> </span></div>
                    {this.state.showDropdown ? dropdownList : null}
                    <Input label='Название скрипта' floatingLabel={true} defaultValue={this.props.script.name || ''}
                           onChange={(e) => {
                               this.setName(e.target.value);
                               this.validateScript(e.target.value);
                           }}/>
                    {this.state.scriptExist ?
                        <span className='input_error'>Скрипт с таким именем уже существует</span> : null}
                    <AceEditor
                        mode='yaml'
                        theme='eclipse'
                        width='100%'
                        onChange={(content) => {
                            this.setContent(content);
                        }}
                        onLoad={::this.setContentOnLoad}
                        value={this.state.content}
                    />

                </div>
            </Form>
            <div>
                <Divider />
                <Button size='small' color='primary' variant='flat'
                        onClick={this.props.cancel} className='modal__btn mui--pull-right'>Отменить</Button>
                <Button size='small' color='primary' variant='flat' onClick={this.editScript}
                        className='modal__btn mui--pull-right'>Сохранить</Button>
            </div>
        </div>
    }
}