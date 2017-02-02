import React, {PropTypes, Component} from 'react';
import FileDescription from '../components/FileDescription';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import Container from 'muicss/lib/react/container';
import TreeView from './tree/TreeView';

export class FilesTree extends Component {

    constructor(props) {
        super(props);

        this.state = {
            showFileDescription: false,
            content: ''
        };
        this.showContent = this.showContent.bind(this);
    }

    componentDidMount() {
        this.props.filesRequest();
    }

    showContent(content) {
        this.setState({
            showFileDescription: true,
            content: content
        })
    }

    render() {

        let _this = this, template, fileDescription;

        if (_this.state.showFileDescription) {

            fileDescription = <FileDescription description={this.state.content}/>;
        }

        if(!this.props.files) {
            template = <div>Данных нету</div>
        } else {
            template = <TreeView groups={this.props.files} showContent={this.showContent} />;
        }

        return <Container>
            <Row>
                <Col md='2' xs='2' lg='2'>
                    <ul className='list mui-list--unstyled'>
                        {template}
                    </ul>
                </Col>
                <Col md='10' xs='10' lg='10'>
                    <div className=''>
                        {fileDescription || ''}
                    </div>
                </Col>
            </Row>
        </Container>
    }
}

FilesTree.propTypes = {
    filesRequest: PropTypes.func.isRequired,
    // error: PropTypes.string.isRequired
};

// let data = [
//     {
//         name: 'riversoft',
//         files: [
//             {
//                 name: 'name1',
//                 content: 'content1'
//             },
//             {
//                 name: 'name2',
//                 content: 'content2'
//             },
//             {
//                 name: 'name3',
//                 content: 'content3'
//             }
//         ]
//     },
//     {
//         name: 'hrenznaetchosoft',
//         files: [
//             {
//                 name: 'name21',
//                 content: 'content21'
//             },
//             {
//                 name: 'name22',
//                 content: 'content22'
//             },
//             {
//                 name: 'name23',
//                 content: 'content23'
//             }
//         ]
//     },
//     {
//         name: 'esheodinspisok',
//         files: [
//             {
//                 name: 'name21',
//                 content: 'content21'
//             },
//             {
//                 name: 'name22',
//                 content: 'content22'
//             },
//             {
//                 name: 'name23',
//                 content: 'content23'
//             },
//             {
//                 name: 'name25',
//                 content: 'gdfsavn klsd flgasfasdhgkgn dfg jhdsgfl h'
//             },
//             {
//                 name: 'name28',
//                 content: 'dg;fakjldkjas asdfgk hds kks dds ghfksbvk'
//             },
//             {
//                 name: 'name20',
//                 content: 'ds;fgkjs;lkjas; asdf lasgf a asdfasgf lasfkas'
//             }
//         ]
//     },
// ];